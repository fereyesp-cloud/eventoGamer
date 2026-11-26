package duoc.eventogamer.dto;

import duoc.eventogamer.model.CanalCompartido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoCompartidoDTOTest {

    @Test
    @DisplayName("EventoCompartidoDTO contiene datos del compartido y enlaces sociales")
    void constructorYGetters() {
        LocalDateTime ahora = LocalDateTime.now();
        EventoCompartidoDTO dto = new EventoCompartidoDTO(
                1L, 2L, "Torneo Valorant", "Valorant", "fernando", CanalCompartido.SITIO_WEB,
                "¡Vamos!", "http://localhost:8080/eventos/2", ahora, Map.of("whatsapp", "https://wa.me/?text=hola"));

        assertEquals("Torneo Valorant", dto.getEventoNombre());
        assertEquals(CanalCompartido.SITIO_WEB, dto.getCanal());
        assertEquals("https://wa.me/?text=hola", dto.getEnlacesRedesSociales().get("whatsapp"));
    }
}
