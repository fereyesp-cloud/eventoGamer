package duoc.eventogamer.service;

import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioDetailsServiceTest {

    @Test
    @DisplayName("loadUserByUsername carga usuario activo desde BD y falla si no existe")
    void cargaUsuarioDesdeRepositorio() {
        UsuarioRepository repo = mock(UsuarioRepository.class);
        Usuario usuario = new Usuario("admin", "$2a$10$hash", Set.of("USER", "ADMIN"));
        when(repo.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(repo.findByUsername("desconocido")).thenReturn(Optional.empty());

        UsuarioDetailsService service = new UsuarioDetailsService(repo);
        UserDetails details = service.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("desconocido"));
    }
}
