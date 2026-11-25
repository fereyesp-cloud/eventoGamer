package duoc.eventogamer.controller;

import duoc.eventogamer.dto.ResumenValoracionesDTO;
import duoc.eventogamer.model.Evento;
import duoc.eventogamer.service.EventoInteraccionService;
import duoc.eventogamer.service.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventoControllerTest {

    @Test
    @DisplayName("listar y detalle retornan las vistas eventos y evento-detalle con sus atributos")
    void listarYDetalleEventos() {
        EventoService eventoService = mock(EventoService.class);
        EventoInteraccionService interaccionService = mock(EventoInteraccionService.class);
        EventoController controller = new EventoController(eventoService, interaccionService);

        when(eventoService.buscarAvanzado(any(), any(), any(), any(), any(), any())).thenReturn(List.of());
        when(eventoService.categorias()).thenReturn(List.of("Torneo"));
        when(eventoService.videojuegos()).thenReturn(List.of("Valorant"));
        when(eventoService.modalidades()).thenReturn(List.of("Presencial", "Online"));
        when(eventoService.ubicaciones()).thenReturn(List.of("Santiago"));

        ConcurrentModel modelListado = new ConcurrentModel();
        assertEquals("eventos", controller.listar(null, null, null, null, null, null, modelListado));

        Evento evento = new Evento(1L, "Torneo", "Desc", "Valorant", "Torneo", "Chile", "Santiago",
                "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0), "Santiago", "Dir", null,
                "Org", List.of(), List.of(), "Req", "Insc", List.of(), "/img/a.jpg",
                true, true, true, false, true, true, LocalDate.of(2026, 10, 20));
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("fernando");
        when(auth.getAuthorities()).thenReturn(List.of());
        when(eventoService.buscarPorId(1L)).thenReturn(Optional.of(evento));
        when(interaccionService.listarMedia(1L)).thenReturn(List.of());
        when(interaccionService.listarComentarios(1L)).thenReturn(List.of());
        when(interaccionService.obtenerResumenValoraciones(1L, "fernando"))
                .thenReturn(new ResumenValoracionesDTO(1L, 5.0, 1L, 5, List.of()));
        when(interaccionService.listarCompartidosPorEvento(1L)).thenReturn(List.of());
        when(interaccionService.construirEnlacesRedesSociales(evento, null)).thenReturn(Map.of());

        ConcurrentModel modelDetalle = new ConcurrentModel();
        assertEquals("evento-detalle", controller.detalle(1L, auth, modelDetalle));
        assertEquals("compartidos", controller.muroCompartidos(new ConcurrentModel()));
    }
}
