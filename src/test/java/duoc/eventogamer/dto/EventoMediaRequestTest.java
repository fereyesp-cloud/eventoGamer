package duoc.eventogamer.dto;

import duoc.eventogamer.model.TipoMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoMediaRequestTest {

    @Test
    @DisplayName("EventoMediaRequest almacena tipo, url y título")
    void constructorYGetters() {
        EventoMediaRequest req = new EventoMediaRequest(TipoMedia.FOTO, "https://cdn.ejemplo.com/a.jpg", "Arena");
        assertEquals(TipoMedia.FOTO, req.getTipo());
        assertEquals("https://cdn.ejemplo.com/a.jpg", req.getUrl());
        assertEquals("Arena", req.getTitulo());
    }
}
