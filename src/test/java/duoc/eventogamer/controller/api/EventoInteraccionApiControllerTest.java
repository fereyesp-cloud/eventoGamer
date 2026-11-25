package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.CanalCompartido;
import duoc.eventogamer.model.TipoMedia;
import duoc.eventogamer.service.EventoInteraccionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventoInteraccionApiControllerTest {

    @Test
    @DisplayName("expone endpoints privados de media, compartir, comentarios y valoraciones")
    void endpointsDeInteraccionRespondenCorrectamente() {
        EventoInteraccionService service = mock(EventoInteraccionService.class);
        EventoInteraccionApiController controller = new EventoInteraccionApiController(service);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("fernando");
        when(auth.getAuthorities()).thenReturn(List.of());

        when(service.agregarMediaPorUrl(eq(1L), eq("fernando"), any())).thenReturn(new EventoMediaDTO());
        when(service.compartirEvento(eq(1L), eq("fernando"), any())).thenReturn(new EventoCompartidoDTO());
        when(service.agregarComentario(eq(1L), eq("fernando"), any())).thenReturn(new ComentarioDTO());
        when(service.valorarEvento(eq(1L), eq("fernando"), any())).thenReturn(new ResumenValoracionesDTO());

        assertEquals(201, controller.agregarMediaPorUrl(1L, new EventoMediaRequest(TipoMedia.FOTO, "https://a.com/a.jpg", "A"), auth).getStatusCode().value());
        assertEquals(201, controller.compartirEvento(1L, new CompartirEventoRequest(CanalCompartido.SITIO_WEB, "Hola"), auth).getStatusCode().value());
        assertEquals(201, controller.agregarComentario(1L, new ComentarioRequest("Buen evento"), auth).getStatusCode().value());
        assertEquals(201, controller.valorarEvento(1L, new ValoracionRequest(5), auth).getStatusCode().value());
        assertEquals(204, controller.eliminarComentario(1L, 10L, auth).getStatusCode().value());
    }
}
