package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValoracionRequestTest {

    @Test
    @DisplayName("ValoracionRequest almacena la puntuación")
    void constructorYGetters() {
        ValoracionRequest req = new ValoracionRequest(5);
        assertEquals(5, req.getPuntuacion());
    }
}
