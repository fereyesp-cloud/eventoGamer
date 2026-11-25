package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventoMediaTest {

    @Test
    @DisplayName("EventoMedia registra tipo, url, título y fechaCreacion")
    void constructorYGetters() {
        Evento evento = new Evento();
        Usuario usuario = new Usuario("fernando", "hash", Set.of("USER"));

        EventoMedia media = new EventoMedia(evento, usuario, TipoMedia.VIDEO,
                "https://www.youtube.com/watch?v=e_E9W2vsRbQ", "Tráiler");
        media.setId(3L);

        assertEquals(3L, media.getId());
        assertEquals(TipoMedia.VIDEO, media.getTipo());
        assertEquals("Tráiler", media.getTitulo());
        assertNotNull(media.getFechaCreacion());
    }
}
