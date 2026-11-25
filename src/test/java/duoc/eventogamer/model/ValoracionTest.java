package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValoracionTest {

    @Test
    @DisplayName("Valoracion guarda puntuación, usuario, evento y fechaActualizacion")
    void constructorYGettersSetters() {
        Evento evento = new Evento();
        Usuario usuario = new Usuario("gina", "hash", Set.of("USER"));

        Valoracion valoracion = new Valoracion(evento, usuario, 5);
        valoracion.setId(2L);

        assertEquals(2L, valoracion.getId());
        assertEquals(5, valoracion.getPuntuacion());
        assertSame(evento, valoracion.getEvento());
        assertSame(usuario, valoracion.getUsuario());
        assertNotNull(valoracion.getFechaActualizacion());
    }
}
