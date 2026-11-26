package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActualizarUsuarioRequestTest {

    @Test
    @DisplayName("ActualizarUsuarioRequest almacena roles, activo y nuevaPassword")
    void constructorYGetters() {
        ActualizarUsuarioRequest req = new ActualizarUsuarioRequest(Set.of("USER", "ADMIN"), true, "NuevaClave2026!");
        assertEquals(Set.of("USER", "ADMIN"), req.getRoles());
        assertTrue(req.getActivo());
        assertEquals("NuevaClave2026!", req.getNuevaPassword());
    }
}
