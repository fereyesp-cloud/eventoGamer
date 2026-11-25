package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    @DisplayName("Usuario inicializa username, password cifrada, roles y activo")
    void constructorYGettersSetters() {
        Usuario usuario = new Usuario("admin", "$2a$10$hash", Set.of("USER", "ADMIN"));
        usuario.setId(1L);

        assertEquals(1L, usuario.getId());
        assertEquals("admin", usuario.getUsername());
        assertEquals("$2a$10$hash", usuario.getPassword());
        assertTrue(usuario.isActivo());
        assertTrue(usuario.getRoles().contains("ADMIN"));
    }
}
