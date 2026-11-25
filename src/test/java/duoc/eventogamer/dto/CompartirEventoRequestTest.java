package duoc.eventogamer.dto;

import duoc.eventogamer.model.CanalCompartido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompartirEventoRequestTest {

    @Test
    @DisplayName("CompartirEventoRequest almacena canal y mensaje")
    void constructorYGetters() {
        CompartirEventoRequest req = new CompartirEventoRequest(CanalCompartido.TWITTER, "¡Súmate!");
        assertEquals(CanalCompartido.TWITTER, req.getCanal());
        assertEquals("¡Súmate!", req.getMensaje());
    }
}
