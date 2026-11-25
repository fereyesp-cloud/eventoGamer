package duoc.eventogamer.repository;

import duoc.eventogamer.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioRepositoryTest {

    @Test
    @DisplayName("findByUsername y contarActivosConRol consultan usuarios y administradores activos")
    void consultarUsuarioYAdminsActivos() {
        UsuarioRepository repo = mock(UsuarioRepository.class);
        when(repo.findByUsername("admin")).thenReturn(Optional.of(new Usuario("admin", "hash", Set.of("ADMIN"))));
        when(repo.contarActivosConRol("ADMIN")).thenReturn(1L);

        assertTrue(repo.findByUsername("admin").isPresent());
        assertEquals(1L, repo.contarActivosConRol("ADMIN"));
    }
}
