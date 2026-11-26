package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumenValoracionesDTOTest {

    @Test
    @DisplayName("ResumenValoracionesDTO almacena promedio, total y miValoracion")
    void constructorYGetters() {
        ResumenValoracionesDTO dto = new ResumenValoracionesDTO(1L, 4.5, 2L, 5, List.of());
        assertEquals(1L, dto.getEventoId());
        assertEquals(4.5, dto.getPromedio());
        assertEquals(2L, dto.getTotalValoraciones());
        assertEquals(5, dto.getMiValoracion());
    }
}
