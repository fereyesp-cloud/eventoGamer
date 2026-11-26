package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanalCompartidoTest {

    @Test
    @DisplayName("CanalCompartido incluye sitio web y redes sociales esperadas")
    void valoresEnum() {
        assertEquals(5, CanalCompartido.values().length);
        assertEquals(CanalCompartido.SITIO_WEB, CanalCompartido.valueOf("SITIO_WEB"));
        assertEquals(CanalCompartido.WHATSAPP, CanalCompartido.valueOf("WHATSAPP"));
    }
}
