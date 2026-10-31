package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import duoc.eventogamer.service.EventoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoApiController {

    private final EventoService eventoService;

    public EventoApiController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * [Pública] Búsqueda y filtrado de eventos.
     * Filtros solicitados:
     * - nombre
     * - videojuego
     * - tipo o categoría
     * - país o ciudad
     * - fecha
     * - modalidad (presencial u online)
     * Retorna información resumida de cada evento (EventoResumenDTO).
     */
    @GetMapping
    public ResponseEntity<List<EventoResumenDTO>> buscarEventos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String videojuego,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String paisCiudad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String modalidad) {

        String catFinal = (categoria != null && !categoria.isBlank()) ? categoria : tipo;
        List<EventoResumenDTO> resultados = eventoService.buscarAvanzado(
                nombre, videojuego, catFinal, paisCiudad, fecha, modalidad
        );
        return ResponseEntity.ok(resultados);
    }

    /**
     * [Privada] Detalle de eventos:
     * Requiere que el usuario esté autenticado mediante token JWT (Bearer Token).
     * Retorna descripción, videojuego, fecha y hora, ubicación o enlace de acceso,
     * organizador, requisitos de participación, información de inscripción y fotografías.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventoDetalleDTO> obtenerDetalleEvento(@PathVariable Long id) {
        EventoDetalleDTO detalle = eventoService.obtenerDetalle(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con id: " + id));
        return ResponseEntity.ok(detalle);
    }
}

