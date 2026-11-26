package duoc.eventogamer.service;

import duoc.eventogamer.dto.ActualizarUsuarioRequest;
import duoc.eventogamer.dto.CrearUsuarioRequest;
import duoc.eventogamer.dto.UsuarioAdminDTO;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioAdminServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioAdminService service;
    private Usuario admin;
    private Usuario fernando;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        service = new UsuarioAdminService(usuarioRepository, new BCryptPasswordEncoder());

        admin = new Usuario("admin", "hash", Set.of("USER", "ADMIN"));
        admin.setId(1L);
        fernando = new Usuario("fernando", "hash", Set.of("USER"));
        fernando.setId(2L);
    }

    @Test
    @DisplayName("crearUsuario cifra contraseña con BCrypt y valida roles permitidos")
    void crearUsuarioConRolesValidos() {
        when(usuarioRepository.existsByUsername("mod_cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(3L);
            return u;
        });

        UsuarioAdminDTO creado = service.crearUsuario(
                new CrearUsuarioRequest("mod_cl", "ClaveSegura2026!", Set.of("ADMIN"), true));

        assertEquals(3L, creado.getId());
        assertEquals("mod_cl", creado.getUsername());
        assertTrue(creado.isAdministrador());
        assertTrue(creado.getRoles().contains("USER"));
        assertTrue(creado.getRoles().contains("ADMIN"));
    }

    @Test
    @DisplayName("impide desactivar la propia cuenta admin y permite actualizar otros usuarios")
    void protegeCuentaPropiaYActualizaOtros() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(fernando));
        when(usuarioRepository.findAllByOrderByIdAsc()).thenReturn(List.of(admin, fernando));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.cambiarEstado(1L, false, "admin"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        UsuarioAdminDTO actualizado = service.actualizarUsuario(
                2L, new ActualizarUsuarioRequest(Set.of("USER", "ADMIN"), true, null), "admin");
        assertTrue(actualizado.isAdministrador());
        assertEquals(2, service.listarTodos().size());
    }
}
