package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.InicioDTO;
import duoc.eventogamer.service.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InicioApiControllerTest {

    @Test
    @DisplayName("obtenerInicio devuelve 200 con InicioDTO desde EventoService")
    void obtenerInicioRetornaOk() {
        EventoService service = mock(EventoService.class);
        when(service.obtenerDatosInicio())
                .thenReturn(new InicioDTO(List.of(), List.of(), List.of("Banner"), "/api/auth/login"));

        InicioApiController controller = new InicioApiController(service);
        ResponseEntity<InicioDTO> response = controller.obtenerInicio();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("/api/auth/login", response.getBody().getLoginUrl());
    }
}
