package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioTest {

    @Test
    @DisplayName("Comentario asigna evento, usuario, contenido y fechaCreacion")
    void constructorYGetters() {
        Evento evento = new Evento();
        Usuario usuario = new Usuario("gina", "hash", Set.of("USER"));

        Comentario comentario = new Comentario(evento, usuario, "¡Excelente torneo!");
        comentario.setId(4L);

        assertEquals(4L, comentario.getId());
        assertSame(evento, comentario.getEvento());
        assertSame(usuario, comentario.getUsuario());
        assertEquals("¡Excelente torneo!", comentario.getContenido());
        assertNotNull(comentario.getFechaCreacion());
    }
}
