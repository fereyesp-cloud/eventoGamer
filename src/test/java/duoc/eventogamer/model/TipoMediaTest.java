package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TipoMediaTest {

    @Test
    @DisplayName("TipoMedia define FOTO y VIDEO")
    void valoresEnum() {
        assertEquals(2, TipoMedia.values().length);
        assertEquals(TipoMedia.FOTO, TipoMedia.valueOf("FOTO"));
        assertEquals(TipoMedia.VIDEO, TipoMedia.valueOf("VIDEO"));
    }
}
