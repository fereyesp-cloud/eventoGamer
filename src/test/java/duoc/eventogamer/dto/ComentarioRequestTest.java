package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComentarioRequestTest {

    @Test
    @DisplayName("ComentarioRequest guarda y devuelve contenido")
    void constructorYGetters() {
        ComentarioRequest req = new ComentarioRequest("Nos vemos en la final");
        assertEquals("Nos vemos en la final", req.getContenido());
    }
}
