package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.InicioDTO;
import duoc.eventogamer.service.EventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inicio")
public class InicioApiController {

    private final EventoService eventoService;

    public InicioApiController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * [Pública] Página / API de inicio:
     * - Muestra los eventos más recientes y populares.
     * - Banners de avisos comerciales.
     * - Enlace / acceso a la página de inicio de sesión.
     */
    @GetMapping
    public ResponseEntity<InicioDTO> obtenerInicio() {
        return ResponseEntity.ok(eventoService.obtenerDatosInicio());
    }
}

