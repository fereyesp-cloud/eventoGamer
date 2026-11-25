package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventoCompartidoTest {

    @Test
    @DisplayName("EventoCompartido almacena canal, mensaje, enlace y fecha")
    void constructorYGetters() {
        Evento evento = new Evento();
        Usuario usuario = new Usuario("fernando", "hash", Set.of("USER"));

        EventoCompartido compartido = new EventoCompartido(
                evento, usuario, CanalCompartido.SITIO_WEB, "¡Participa!", "http://localhost:8080/eventos/1");
        compartido.setId(9L);

        assertEquals(9L, compartido.getId());
        assertEquals(CanalCompartido.SITIO_WEB, compartido.getCanal());
        assertEquals("¡Participa!", compartido.getMensaje());
        assertNotNull(compartido.getFechaCompartido());
    }
}
