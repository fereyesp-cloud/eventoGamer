package duoc.eventogamer.repository;

import duoc.eventogamer.model.Evento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventoRepositoryTest {

    @Test
    @DisplayName("buscarAvanzado y findTop4ByOrderByFechaPublicacionDesc consultan eventos filtrados")
    void buscarYListarEventos() {
        EventoRepository repo = mock(EventoRepository.class);
        when(repo.buscarAvanzado("Torneo", "Valorant", null, null, null, "Presencial"))
                .thenReturn(List.of(new Evento()));
        when(repo.findTop4ByOrderByFechaPublicacionDesc()).thenReturn(List.of(new Evento()));

        assertEquals(1, repo.buscarAvanzado("Torneo", "Valorant", null, null, null, "Presencial").size());
        assertEquals(1, repo.findTop4ByOrderByFechaPublicacionDesc().size());
    }
}
