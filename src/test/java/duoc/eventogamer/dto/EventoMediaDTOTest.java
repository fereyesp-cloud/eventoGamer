package duoc.eventogamer.dto;

import duoc.eventogamer.model.TipoMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoMediaDTOTest {

    @Test
    @DisplayName("EventoMediaDTO guarda tipo, url, embedUrl y autor")
    void constructorYGetters() {
        LocalDateTime ahora = LocalDateTime.now();
        EventoMediaDTO dto = new EventoMediaDTO(1L, 2L, "gina", TipoMedia.VIDEO,
                "https://youtu.be/e_E9W2vsRbQ", "https://www.youtube-nocookie.com/embed/e_E9W2vsRbQ", "Clip", ahora);

        assertEquals(TipoMedia.VIDEO, dto.getTipo());
        assertEquals("gina", dto.getUsuario());
        assertEquals("https://www.youtube-nocookie.com/embed/e_E9W2vsRbQ", dto.getEmbedUrl());
    }
}
