package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventoDetalleDTOTest {

    @Test
    @DisplayName("EventoDetalleDTO expone información completa del evento y métricas sociales")
    void constructorYSetters() {
        EventoDetalleDTO dto = new EventoDetalleDTO(
                1L, "Torneo", "Desc", "Valorant", "Torneo", "Chile", "Santiago", "Presencial",
                LocalDate.of(2026, 11, 14), LocalTime.of(10, 0), "Santiago", null, "Org",
                "Req", "Insc", List.of("/img/a.jpg"), true);
        dto.setPublicadoPor("fernando");
        dto.setPromedioValoracion(4.8);
        dto.setTotalValoraciones(5L);
        dto.setTotalComentarios(3L);
        dto.setTotalCompartidos(2L);

        assertEquals("fernando", dto.getPublicadoPor());
        assertEquals(4.8, dto.getPromedioValoracion());
        assertEquals(5L, dto.getTotalValoraciones());
        assertTrue(dto.isPopular());
    }
}
