package duoc.eventogamer.repository;

import duoc.eventogamer.model.CanalCompartido;
import duoc.eventogamer.model.EventoCompartido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventoCompartidoRepositoryTest {

    @Test
    @DisplayName("findByCanalOrderByFechaCompartidoDesc filtra los eventos compartidos en el sitio web")
    void consultarPorCanal() {
        EventoCompartidoRepository repo = mock(EventoCompartidoRepository.class);
        when(repo.findByCanalOrderByFechaCompartidoDesc(CanalCompartido.SITIO_WEB))
                .thenReturn(List.of(new EventoCompartido()));
        when(repo.countByEventoId(1L)).thenReturn(2L);

        assertEquals(1, repo.findByCanalOrderByFechaCompartidoDesc(CanalCompartido.SITIO_WEB).size());
        assertEquals(2L, repo.countByEventoId(1L));
    }
}
