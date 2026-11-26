package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CrearUsuarioRequestTest {

    @Test
    @DisplayName("CrearUsuarioRequest guarda username, password, roles y activo")
    void constructorYGetters() {
        CrearUsuarioRequest req = new CrearUsuarioRequest("nuevo_admin", "ClaveSegura2026!", Set.of("ADMIN"), true);
        assertEquals("nuevo_admin", req.getUsername());
        assertEquals("ClaveSegura2026!", req.getPassword());
        assertEquals(Set.of("ADMIN"), req.getRoles());
        assertTrue(req.getActivo());
    }
}
