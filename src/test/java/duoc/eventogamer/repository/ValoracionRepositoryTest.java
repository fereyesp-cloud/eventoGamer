package duoc.eventogamer.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ValoracionRepositoryTest {

    @Test
    @DisplayName("obtenerPromedioPorEventoId y countByEventoId calculan métricas de valoración")
    void calcularPromedioYTotal() {
        ValoracionRepository repo = mock(ValoracionRepository.class);
        when(repo.obtenerPromedioPorEventoId(1L)).thenReturn(4.5);
        when(repo.countByEventoId(1L)).thenReturn(2L);

        assertEquals(4.5, repo.obtenerPromedioPorEventoId(1L));
        assertEquals(2L, repo.countByEventoId(1L));
    }
}
