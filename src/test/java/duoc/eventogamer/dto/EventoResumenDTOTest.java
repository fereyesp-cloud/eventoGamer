package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EventoResumenDTOTest {

    @Test
    @DisplayName("EventoResumenDTO almacena los campos resumidos del evento")
    void constructorYGetters() {
        EventoResumenDTO dto = new EventoResumenDTO(1L, "Copa LoL", "League of Legends", "Torneo",
                "Chile", "Online", "Online", LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), "/img/lol.jpg", true);
        assertEquals("Copa LoL", dto.getNombre());
        assertEquals("Online", dto.getModalidad());
        assertTrue(dto.isPopular());
    }
}
