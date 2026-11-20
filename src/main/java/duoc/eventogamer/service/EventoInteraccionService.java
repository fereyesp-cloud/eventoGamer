package duoc.eventogamer.service;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.*;
import duoc.eventogamer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventoInteraccionService {

    private static final String EVENTO_NO_ENCONTRADO = "Evento no encontrado";
    private static final String USUARIO_NO_ENCONTRADO = "Usuario autenticado no encontrado";

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoMediaRepository mediaRepository;
    private final EventoCompartidoRepository compartidoRepository;
    private final ComentarioRepository comentarioRepository;
    private final ValoracionRepository valoracionRepository;
    private final MediaStorageService mediaStorageService;
    private final String baseUrl;

    public EventoInteraccionService(EventoRepository eventoRepository,
                                    UsuarioRepository usuarioRepository,
                                    EventoMediaRepository mediaRepository,
                                    EventoCompartidoRepository compartidoRepository,
                                    ComentarioRepository comentarioRepository,
                                    ValoracionRepository valoracionRepository,
                                    MediaStorageService mediaStorageService,
                                    @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mediaRepository = mediaRepository;
        this.compartidoRepository = compartidoRepository;
        this.comentarioRepository = comentarioRepository;
        this.valoracionRepository = valoracionRepository;
        this.mediaStorageService = mediaStorageService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    // ==========================================
    // 1. Multimedia (Fotos y Videos) [Privada]
    // ==========================================

    @Transactional(readOnly = true)
    public List<EventoMediaDTO> listarMedia(Long eventoId) {
        verificarEventoExiste(eventoId);
        return mediaRepository.findByEventoIdOrderByFechaCreacionDesc(eventoId).stream()
                .map(this::toMediaDTO)
                .toList();
    }

    @Transactional
    public EventoMediaDTO agregarMediaPorUrl(Long eventoId, String username, EventoMediaRequest request) {
        Evento evento = obtenerEvento(eventoId);
        Usuario usuario = obtenerUsuario(username);
        String urlSegura = mediaStorageService.validarYNormalizarUrl(request.getUrl());
        String titulo = limpiarTextoOpcional(request.getTitulo(), 120);

        EventoMedia media = mediaRepository.save(
                new EventoMedia(evento, usuario, request.getTipo(), urlSegura, titulo)
        );
        return toMediaDTO(media);
    }

    @Transactional
    public EventoMediaDTO subirMediaArchivo(Long eventoId, String username, TipoMedia tipo,
                                            MultipartFile archivo, String tituloOpcional) {
        Evento evento = obtenerEvento(eventoId);
        Usuario usuario = obtenerUsuario(username);
        String urlGuardada = mediaStorageService.guardarArchivo(archivo, tipo);
        String titulo = limpiarTextoOpcional(tituloOpcional, 120);

        EventoMedia media = mediaRepository.save(
                new EventoMedia(evento, usuario, tipo, urlGuardada, titulo)
        );
        return toMediaDTO(media);
    }

    @Transactional
    public void eliminarMedia(Long eventoId, Long mediaId, String username, boolean esAdmin) {
        EventoMedia media = mediaRepository.findById(mediaId)
                .filter(m -> m.getEvento().getId().equals(eventoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo multimedia no encontrado"));

        if (!esAdmin && !media.getUsuario().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este archivo multimedia");
        }
        mediaRepository.delete(media);
    }

    // ==========================================
    // 2. Compartir Eventos (Sitio Web y Redes) [Privada]
    // ==========================================

    @Transactional
    public EventoCompartidoDTO compartirEvento(Long eventoId, String username, CompartirEventoRequest request) {
        Evento evento = obtenerEvento(eventoId);
        Usuario usuario = obtenerUsuario(username);
        String mensaje = limpiarTextoOpcional(request.getMensaje(), 300);

        Map<String, String> enlaces = construirEnlacesRedesSociales(evento, mensaje);
        String enlaceDestino = resolverEnlaceCanal(request.getCanal(), evento, enlaces);

        EventoCompartido guardado = compartidoRepository.save(
                new EventoCompartido(evento, usuario, request.getCanal(), mensaje, enlaceDestino)
        );
        return toCompartidoDTO(guardado, enlaces);
    }

    @Transactional(readOnly = true)
    public List<EventoCompartidoDTO> listarCompartidosPorEvento(Long eventoId) {
        verificarEventoExiste(eventoId);
        return compartidoRepository.findByEventoIdOrderByFechaCompartidoDesc(eventoId).stream()
                .map(c -> toCompartidoDTO(c, construirEnlacesRedesSociales(c.getEvento(), c.getMensaje())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventoCompartidoDTO> listarCompartidosEnSitioWeb() {
        return compartidoRepository.findByCanalOrderByFechaCompartidoDesc(CanalCompartido.SITIO_WEB).stream()
                .map(c -> toCompartidoDTO(c, construirEnlacesRedesSociales(c.getEvento(), c.getMensaje())))
                .toList();
    }

    public Map<String, String> construirEnlacesRedesSociales(Evento evento, String mensajeOpcional) {
        String urlEvento = baseUrl + "/eventos/" + evento.getId();
        String textoBase = (mensajeOpcional != null && !mensajeOpcional.isBlank())
                ? mensajeOpcional + " - " + evento.getNombre() + " (" + evento.getVideojuego() + ")"
                : "¡Mira este evento gamer! " + evento.getNombre() + " (" + evento.getVideojuego() + ")";

        String encodedUrl = URLEncoder.encode(urlEvento, StandardCharsets.UTF_8);
        String encodedTexto = URLEncoder.encode(textoBase, StandardCharsets.UTF_8);
        String encodedWhatsapp = URLEncoder.encode(textoBase + " " + urlEvento, StandardCharsets.UTF_8);

        Map<String, String> enlaces = new LinkedHashMap<>();
        enlaces.put("sitioWeb", urlEvento);
        enlaces.put("whatsapp", "https://wa.me/?text=" + encodedWhatsapp);
        enlaces.put("twitter", "https://twitter.com/intent/tweet?text=" + encodedTexto + "&url=" + encodedUrl);
        enlaces.put("facebook", "https://www.facebook.com/sharer/sharer.php?u=" + encodedUrl);
        enlaces.put("linkedin", "https://www.linkedin.com/sharing/share-offsite/?url=" + encodedUrl);
        return enlaces;
    }

    // ==========================================
    // 3. Comentarios [Privada]
    // ==========================================

    @Transactional(readOnly = true)
    public List<ComentarioDTO> listarComentarios(Long eventoId) {
        verificarEventoExiste(eventoId);
        return comentarioRepository.findByEventoIdOrderByFechaCreacionDesc(eventoId).stream()
                .map(this::toComentarioDTO)
                .toList();
    }

    @Transactional
    public ComentarioDTO agregarComentario(Long eventoId, String username, ComentarioRequest request) {
        Evento evento = obtenerEvento(eventoId);
        Usuario usuario = obtenerUsuario(username);
        String contenido = request.getContenido() != null ? request.getContenido().trim() : "";

        if (contenido.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El comentario debe tener al menos 2 caracteres");
        }

        Comentario guardado = comentarioRepository.save(new Comentario(evento, usuario, contenido));
        return toComentarioDTO(guardado);
    }

    @Transactional
    public void eliminarComentario(Long eventoId, Long comentarioId, String username, boolean esAdmin) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .filter(c -> c.getEvento().getId().equals(eventoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));

        if (!esAdmin && !comentario.getUsuario().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este comentario");
        }
        comentarioRepository.delete(comentario);
    }

    // ==========================================
    // 4. Valoraciones (1 a 5 estrellas) [Privada]
    // ==========================================

    @Transactional(readOnly = true)
    public ResumenValoracionesDTO obtenerResumenValoraciones(Long eventoId, String username) {
        verificarEventoExiste(eventoId);
        List<Valoracion> lista = valoracionRepository.findByEventoIdOrderByFechaActualizacionDesc(eventoId);
        Double promedioRaw = valoracionRepository.obtenerPromedioPorEventoId(eventoId);
        double promedio = promedioRaw != null ? Math.round(promedioRaw * 10.0) / 10.0 : 0.0;

        Integer miValoracion = lista.stream()
                .filter(v -> v.getUsuario().getUsername().equals(username))
                .map(Valoracion::getPuntuacion)
                .findFirst()
                .orElse(null);

        List<ValoracionDTO> dtos = lista.stream().map(this::toValoracionDTO).toList();
        return new ResumenValoracionesDTO(eventoId, promedio, lista.size(), miValoracion, dtos);
    }

    @Transactional
    public ResumenValoracionesDTO valorarEvento(Long eventoId, String username, ValoracionRequest request) {
        Evento evento = obtenerEvento(eventoId);
        Usuario usuario = obtenerUsuario(username);

        Optional<Valoracion> existente = valoracionRepository.findByEventoIdAndUsuarioId(eventoId, usuario.getId());
        if (existente.isPresent()) {
            Valoracion valoracion = existente.get();
            valoracion.setPuntuacion(request.getPuntuacion());
            valoracion.setFechaActualizacion(LocalDateTime.now());
            valoracionRepository.save(valoracion);
        } else {
            valoracionRepository.save(new Valoracion(evento, usuario, request.getPuntuacion()));
        }

        return obtenerResumenValoraciones(eventoId, username);
    }

    // ==========================================
    // Métricas resumidas para EventoDetalleDTO
    // ==========================================

    @Transactional(readOnly = true)
    public double obtenerPromedioValoracion(Long eventoId) {
        Double promedio = valoracionRepository.obtenerPromedioPorEventoId(eventoId);
        return promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0;
    }

    @Transactional(readOnly = true)
    public long contarValoraciones(Long eventoId) {
        return valoracionRepository.countByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public long contarComentarios(Long eventoId) {
        return comentarioRepository.countByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public long contarCompartidos(Long eventoId) {
        return compartidoRepository.countByEventoId(eventoId);
    }

    // ==========================================
    // Helpers privados
    // ==========================================

    private Evento obtenerEvento(Long eventoId) {
        return eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENTO_NO_ENCONTRADO));
    }

    private void verificarEventoExiste(Long eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENTO_NO_ENCONTRADO);
        }
    }

    private Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, USUARIO_NO_ENCONTRADO));
    }

    private String limpiarTextoOpcional(String texto, int maxLen) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.length() > maxLen ? limpio.substring(0, maxLen) : limpio;
    }

    private String resolverEnlaceCanal(CanalCompartido canal, Evento evento, Map<String, String> enlaces) {
        return switch (canal) {
            case SITIO_WEB -> baseUrl + "/eventos/" + evento.getId();
            case WHATSAPP -> enlaces.get("whatsapp");
            case TWITTER -> enlaces.get("twitter");
            case FACEBOOK -> enlaces.get("facebook");
            case LINKEDIN -> enlaces.get("linkedin");
        };
    }

    private EventoMediaDTO toMediaDTO(EventoMedia media) {
        return new EventoMediaDTO(
                media.getId(),
                media.getEvento().getId(),
                media.getUsuario().getUsername(),
                media.getTipo(),
                media.getUrl(),
                mediaStorageService.extraerEmbedUrl(media.getUrl(), media.getTipo()),
                media.getTitulo(),
                media.getFechaCreacion()
        );
    }

    private EventoCompartidoDTO toCompartidoDTO(EventoCompartido compartido, Map<String, String> enlaces) {
        return new EventoCompartidoDTO(
                compartido.getId(),
                compartido.getEvento().getId(),
                compartido.getEvento().getNombre(),
                compartido.getEvento().getVideojuego(),
                compartido.getUsuario().getUsername(),
                compartido.getCanal(),
                compartido.getMensaje(),
                compartido.getEnlaceCompartido(),
                compartido.getFechaCompartido(),
                enlaces
        );
    }

    private ComentarioDTO toComentarioDTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getId(),
                comentario.getEvento().getId(),
                comentario.getUsuario().getUsername(),
                comentario.getContenido(),
                comentario.getFechaCreacion()
        );
    }

    private ValoracionDTO toValoracionDTO(Valoracion valoracion) {
        return new ValoracionDTO(
                valoracion.getId(),
                valoracion.getEvento().getId(),
                valoracion.getUsuario().getUsername(),
                valoracion.getPuntuacion(),
                valoracion.getFechaActualizacion()
        );
    }
}
