package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValoracionDTOTest {

    @Test
    @DisplayName("ValoracionDTO almacena puntuación, usuario y fecha")
    void constructorYGetters() {
        LocalDateTime ahora = LocalDateTime.now();
        ValoracionDTO dto = new ValoracionDTO(1L, 2L, "gina", 4, ahora);
        assertEquals(4, dto.getPuntuacion());
        assertEquals("gina", dto.getUsuario());
        assertEquals(ahora, dto.getFechaActualizacion());
    }
}
