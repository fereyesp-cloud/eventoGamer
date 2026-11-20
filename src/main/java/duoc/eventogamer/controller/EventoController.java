package duoc.eventogamer.controller;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.CanalCompartido;
import duoc.eventogamer.model.Evento;
import duoc.eventogamer.model.TipoMedia;
import duoc.eventogamer.service.EventoInteraccionService;
import duoc.eventogamer.service.EventoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class EventoController {

    private static final String REDIRECT_EVENTO = "redirect:/eventos/";
    private static final String ATTR_EXITO = "mensajeExito";
    private static final String ATTR_ERROR = "mensajeError";

    private final EventoService eventoService;
    private final EventoInteraccionService interaccionService;

    public EventoController(EventoService eventoService,
                            EventoInteraccionService interaccionService) {
        this.eventoService = eventoService;
        this.interaccionService = interaccionService;
    }

    /**
     * [Pública] Búsqueda y listado de eventos con todos los filtros solicitados:
     * nombre, videojuego, tipo o categoría, país o ciudad, fecha y modalidad.
     */
    @GetMapping("/eventos")
    public String listar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String videojuego,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String paisCiudad,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         @RequestParam(required = false) String modalidad,
                         Model model) {

        List<EventoResumenDTO> eventos = eventoService.buscarAvanzado(
                nombre, videojuego, categoria, paisCiudad, fecha, modalidad
        );

        model.addAttribute("eventos", eventos);
        model.addAttribute("categorias", eventoService.categorias());
        model.addAttribute("videojuegos", eventoService.videojuegos());
        model.addAttribute("modalidades", eventoService.modalidades());
        model.addAttribute("ubicaciones", eventoService.ubicaciones());

        model.addAttribute("nombreSel", nombre);
        model.addAttribute("videojuegoSel", videojuego);
        model.addAttribute("categoriaSel", categoria);
        model.addAttribute("paisCiudadSel", paisCiudad);
        model.addAttribute("fechaSel", fecha);
        model.addAttribute("modalidadSel", modalidad);

        return "eventos";
    }

    /**
     * [Privada] Detalle del evento en vista web con galería multimedia,
     * compartir en sitio web/redes sociales, comentarios y valoraciones.
     */
    @GetMapping("/eventos/{id}")
    public String detalle(@PathVariable Long id, Authentication authentication, Model model) {
        Evento evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String username = authentication.getName();
        List<EventoMediaDTO> multimedia = interaccionService.listarMedia(id);

        model.addAttribute("evento", evento);
        model.addAttribute("usuarioActual", username);
        model.addAttribute("esAdmin", esAdmin(authentication));
        model.addAttribute("fotosMedia", multimedia.stream().filter(m -> m.getTipo() == TipoMedia.FOTO).toList());
        model.addAttribute("videosMedia", multimedia.stream().filter(m -> m.getTipo() == TipoMedia.VIDEO).toList());
        model.addAttribute("comentarios", interaccionService.listarComentarios(id));
        model.addAttribute("valoraciones", interaccionService.obtenerResumenValoraciones(id, username));
        model.addAttribute("compartidos", interaccionService.listarCompartidosPorEvento(id));
        model.addAttribute("enlacesRedes", interaccionService.construirEnlacesRedesSociales(evento, null));
        return "evento-detalle";
    }

    /**
     * [Privada] Agregar foto o video por URL a un evento publicado.
     */
    @PostMapping("/eventos/{id}/media")
    public String agregarMediaUrl(@PathVariable Long id,
                                  @RequestParam("tipo") TipoMedia tipo,
                                  @RequestParam("url") String url,
                                  @RequestParam(value = "titulo", required = false) String titulo,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            interaccionService.agregarMediaPorUrl(id, authentication.getName(), new EventoMediaRequest(tipo, url, titulo));
            redirectAttributes.addFlashAttribute(ATTR_EXITO,
                    tipo == TipoMedia.FOTO ? "Fotografía agregada al evento." : "Video agregado al evento.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#multimedia";
    }

    /**
     * [Privada] Subir archivo de foto o video a un evento publicado.
     */
    @PostMapping("/eventos/{id}/media/upload")
    public String subirMediaArchivo(@PathVariable Long id,
                                    @RequestParam("tipo") TipoMedia tipo,
                                    @RequestParam("archivo") MultipartFile archivo,
                                    @RequestParam(value = "titulo", required = false) String titulo,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            interaccionService.subirMediaArchivo(id, authentication.getName(), tipo, archivo, titulo);
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Archivo multimedia subido correctamente.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#multimedia";
    }

    /**
     * [Privada] Eliminar un elemento multimedia propio (o como administrador).
     */
    @PostMapping("/eventos/{id}/media/{mediaId}/eliminar")
    public String eliminarMedia(@PathVariable Long id,
                                @PathVariable Long mediaId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            interaccionService.eliminarMedia(id, mediaId, authentication.getName(), esAdmin(authentication));
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Elemento multimedia eliminado.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#multimedia";
    }

    /**
     * [Privada] Compartir un evento en el sitio web o registrar compartido hacia redes sociales.
     */
    @PostMapping("/eventos/{id}/compartir")
    public String compartirEvento(@PathVariable Long id,
                                  @RequestParam("canal") CanalCompartido canal,
                                  @RequestParam(value = "mensaje", required = false) String mensaje,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            EventoCompartidoDTO compartido = interaccionService.compartirEvento(
                    id, authentication.getName(), new CompartirEventoRequest(canal, mensaje));
            if (canal == CanalCompartido.SITIO_WEB) {
                redirectAttributes.addFlashAttribute(ATTR_EXITO,
                        "¡Evento compartido en el muro de la comunidad!");
            } else {
                redirectAttributes.addFlashAttribute(ATTR_EXITO,
                        "Compartido registrado para " + canal.name() + ".");
                redirectAttributes.addFlashAttribute("enlaceRedSocial", compartido.getEnlaceCompartido());
                redirectAttributes.addFlashAttribute("canalRedSocial", canal.name());
            }
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#compartir";
    }

    /**
     * [Privada] Muro comunitario con eventos compartidos en el sitio web.
     */
    @GetMapping("/compartidos")
    public String muroCompartidos(Model model) {
        model.addAttribute("compartidos", interaccionService.listarCompartidosEnSitioWeb());
        return "compartidos";
    }

    /**
     * [Privada] Publicar un comentario en el evento.
     */
    @PostMapping("/eventos/{id}/comentarios")
    public String agregarComentario(@PathVariable Long id,
                                    @RequestParam("contenido") String contenido,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            interaccionService.agregarComentario(id, authentication.getName(), new ComentarioRequest(contenido));
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Comentario publicado correctamente.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#comentarios";
    }

    /**
     * [Privada] Eliminar un comentario propio (o como administrador).
     */
    @PostMapping("/eventos/{id}/comentarios/{comentarioId}/eliminar")
    public String eliminarComentario(@PathVariable Long id,
                                     @PathVariable Long comentarioId,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            interaccionService.eliminarComentario(id, comentarioId, authentication.getName(), esAdmin(authentication));
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Comentario eliminado.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#comentarios";
    }

    /**
     * [Privada] Valorar un evento publicado (1 a 5 estrellas).
     */
    @PostMapping("/eventos/{id}/valoraciones")
    public String valorarEvento(@PathVariable Long id,
                                @RequestParam("puntuacion") Integer puntuacion,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, "Selecciona una valoración válida entre 1 y 5 estrellas.");
            return REDIRECT_EVENTO + id + "#valoraciones";
        }
        try {
            interaccionService.valorarEvento(id, authentication.getName(), new ValoracionRequest(puntuacion));
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Tu valoración de " + puntuacion + " ★ ha sido guardada.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_EVENTO + id + "#valoraciones";
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
