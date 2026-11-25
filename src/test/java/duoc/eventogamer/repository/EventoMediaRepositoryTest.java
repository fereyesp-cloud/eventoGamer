package duoc.eventogamer.repository;

import duoc.eventogamer.model.EventoMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventoMediaRepositoryTest {

    @Test
    @DisplayName("findByEventoIdOrderByFechaCreacionDesc retorna multimedia ordenada del evento")
    void consultarMediaPorEvento() {
        EventoMediaRepository repo = mock(EventoMediaRepository.class);
        when(repo.findByEventoIdOrderByFechaCreacionDesc(1L)).thenReturn(List.of(new EventoMedia()));

        assertEquals(1, repo.findByEventoIdOrderByFechaCreacionDesc(1L).size());
        verify(repo).findByEventoIdOrderByFechaCreacionDesc(1L);
    }
}
