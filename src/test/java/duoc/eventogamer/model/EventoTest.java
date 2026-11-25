package duoc.eventogamer.model;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventoTest {

    @Test
    @DisplayName("toResumenDTO y toDetalleDTO convierten correctamente la entidad Evento")
    void conversionADtos() {
        Evento evento = new Evento(1L, "Torneo Valorant", "Descripción", "Valorant", "Torneo",
                "Chile", "Santiago", "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0),
                "Santiago", "Av. O'Higgins 3349", null, "Gamer Chile", List.of("Gamer Chile"),
                List.of("Caster"), "Req", "Insc", List.of("/img/v1.jpg"), "/img/v1.jpg",
                true, true, true, false, true, true, LocalDate.of(2026, 10, 20));
        Usuario creador = new Usuario("fernando", "hash", Set.of("USER"));
        evento.setCreador(creador);

        EventoResumenDTO resumen = evento.toResumenDTO();
        EventoDetalleDTO detalle = evento.toDetalleDTO();

        assertEquals("Torneo Valorant", resumen.getNombre());
        assertEquals("Av. O'Higgins 3349", detalle.getUbicacion());
        assertEquals(1, detalle.getFotografias().size());
        assertSame(creador, evento.getCreador());
    }
}
