package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComentarioDTOTest {

    @Test
    @DisplayName("ComentarioDTO almacena id, eventoId, usuario, contenido y fecha")
    void constructorYGetters() {
        LocalDateTime ahora = LocalDateTime.now();
        ComentarioDTO dto = new ComentarioDTO(1L, 2L, "fernando", "Gran evento", ahora);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getEventoId());
        assertEquals("fernando", dto.getUsuario());
        assertEquals("Gran evento", dto.getContenido());
        assertEquals(ahora, dto.getFechaCreacion());
    }
}
