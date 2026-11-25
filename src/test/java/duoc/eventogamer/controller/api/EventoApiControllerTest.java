package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import duoc.eventogamer.service.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventoApiControllerTest {

    @Test
    @DisplayName("buscarEventos y obtenerDetalleEvento retornan resultados y 404 si no existe")
    void buscarYDetalleEvento() {
        EventoService service = mock(EventoService.class);
        EventoApiController controller = new EventoApiController(service);

        when(service.buscarAvanzado("Torneo", null, "Torneo", null, null, null))
                .thenReturn(List.of(new EventoResumenDTO()));
        when(service.obtenerDetalle(1L)).thenReturn(Optional.of(new EventoDetalleDTO()));
        when(service.obtenerDetalle(99L)).thenReturn(Optional.empty());

        ResponseEntity<List<EventoResumenDTO>> lista = controller.buscarEventos("Torneo", null, null, "Torneo", null, null, null);
        assertEquals(200, lista.getStatusCode().value());
        assertEquals(1, lista.getBody().size());

        assertEquals(200, controller.obtenerDetalleEvento(1L).getStatusCode().value());
        assertThrows(ResponseStatusException.class, () -> controller.obtenerDetalleEvento(99L));
    }
}
