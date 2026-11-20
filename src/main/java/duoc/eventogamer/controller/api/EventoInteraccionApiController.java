package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.TipoMedia;
import duoc.eventogamer.service.EventoInteraccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * APIs privadas (requieren token JWT) para interactuar con eventos publicados:
 * - Agregar y consultar fotos y videos.
 * - Compartir eventos en el sitio web y en redes sociales.
 * - Comentar y valorar eventos publicados.
 */
@RestController
@RequestMapping("/api/eventos")
public class EventoInteraccionApiController {

    private final EventoInteraccionService interaccionService;

    public EventoInteraccionApiController(EventoInteraccionService interaccionService) {
        this.interaccionService = interaccionService;
    }

    // ==========================================
    // [Privada] Fotos y Videos
    // ==========================================

    @GetMapping("/{id}/media")
    public ResponseEntity<List<EventoMediaDTO>> listarMedia(@PathVariable Long id) {
        return ResponseEntity.ok(interaccionService.listarMedia(id));
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<EventoMediaDTO> agregarMediaPorUrl(
            @PathVariable Long id,
            @Valid @RequestBody EventoMediaRequest request,
            Authentication authentication) {
        EventoMediaDTO creado = interaccionService.agregarMediaPorUrl(id, authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PostMapping(value = "/{id}/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventoMediaDTO> subirMediaArchivo(
            @PathVariable Long id,
            @RequestParam("tipo") TipoMedia tipo,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "titulo", required = false) String titulo,
            Authentication authentication) {
        EventoMediaDTO creado = interaccionService.subirMediaArchivo(
                id, authentication.getName(), tipo, archivo, titulo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public ResponseEntity<Void> eliminarMedia(
            @PathVariable Long id,
            @PathVariable Long mediaId,
            Authentication authentication) {
        interaccionService.eliminarMedia(id, mediaId, authentication.getName(), esAdmin(authentication));
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // [Privada] Compartir en Sitio Web y Redes Sociales
    // ==========================================

    @GetMapping("/compartidos")
    public ResponseEntity<List<EventoCompartidoDTO>> listarCompartidosSitioWeb() {
        return ResponseEntity.ok(interaccionService.listarCompartidosEnSitioWeb());
    }

    @GetMapping("/{id}/compartidos")
    public ResponseEntity<List<EventoCompartidoDTO>> listarCompartidosEvento(@PathVariable Long id) {
        return ResponseEntity.ok(interaccionService.listarCompartidosPorEvento(id));
    }

    @PostMapping("/{id}/compartir")
    public ResponseEntity<EventoCompartidoDTO> compartirEvento(
            @PathVariable Long id,
            @Valid @RequestBody CompartirEventoRequest request,
            Authentication authentication) {
        EventoCompartidoDTO compartido = interaccionService.compartirEvento(
                id, authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(compartido);
    }

    // ==========================================
    // [Privada] Comentarios y Valoraciones
    // ==========================================

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> listarComentarios(@PathVariable Long id) {
        return ResponseEntity.ok(interaccionService.listarComentarios(id));
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioDTO> agregarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioRequest request,
            Authentication authentication) {
        ComentarioDTO comentario = interaccionService.agregarComentario(
                id, authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
    }

    @DeleteMapping("/{id}/comentarios/{comentarioId}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Long id,
            @PathVariable Long comentarioId,
            Authentication authentication) {
        interaccionService.eliminarComentario(id, comentarioId, authentication.getName(), esAdmin(authentication));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/valoraciones")
    public ResponseEntity<ResumenValoracionesDTO> obtenerValoraciones(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(interaccionService.obtenerResumenValoraciones(id, authentication.getName()));
    }

    @PostMapping("/{id}/valoraciones")
    public ResponseEntity<ResumenValoracionesDTO> valorarEvento(
            @PathVariable Long id,
            @Valid @RequestBody ValoracionRequest request,
            Authentication authentication) {
        ResumenValoracionesDTO resumen = interaccionService.valorarEvento(
                id, authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumen);
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
