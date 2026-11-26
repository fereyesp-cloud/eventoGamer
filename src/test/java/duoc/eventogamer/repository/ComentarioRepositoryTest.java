package duoc.eventogamer.repository;

import duoc.eventogamer.model.Comentario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ComentarioRepositoryTest {

    @Test
    @DisplayName("findByEventoIdOrderByFechaCreacionDesc y countByEventoId consultan comentarios del evento")
    void consultarYContarComentarios() {
        ComentarioRepository repo = mock(ComentarioRepository.class);
        when(repo.findByEventoIdOrderByFechaCreacionDesc(1L)).thenReturn(List.of(new Comentario()));
        when(repo.countByEventoId(1L)).thenReturn(1L);

        assertEquals(1, repo.findByEventoIdOrderByFechaCreacionDesc(1L).size());
        assertEquals(1L, repo.countByEventoId(1L));
    }
}
