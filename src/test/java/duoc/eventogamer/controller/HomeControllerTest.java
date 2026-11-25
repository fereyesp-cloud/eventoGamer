package duoc.eventogamer.controller;

import duoc.eventogamer.service.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Test
    @DisplayName("inicio carga eventos recientes, populares y banners en la vista index")
    void inicioRetornaVistaIndex() {
        EventoService service = mock(EventoService.class);
        when(service.obtenerRecientes()).thenReturn(List.of());
        when(service.obtenerPopulares()).thenReturn(List.of());
        when(service.obtenerBanners()).thenReturn(List.of("Banner 1"));
        when(service.proximos()).thenReturn(List.of());
        when(service.destacados()).thenReturn(List.of());

        HomeController controller = new HomeController(service);
        ConcurrentModel model = new ConcurrentModel();

        assertEquals("index", controller.inicio(model));
        assertTrue(model.containsAttribute("recientes"));
        assertTrue(model.containsAttribute("populares"));
        assertTrue(model.containsAttribute("banners"));
    }
}
