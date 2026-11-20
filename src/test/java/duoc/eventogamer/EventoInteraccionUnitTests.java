package duoc.eventogamer;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.*;
import duoc.eventogamer.repository.*;
import duoc.eventogamer.service.EventoInteraccionService;
import duoc.eventogamer.service.MediaStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventoInteraccionUnitTests {

    @TempDir
    Path tempDir;

    private MediaStorageService mediaStorageService;
    private EventoRepository eventoRepository;
    private UsuarioRepository usuarioRepository;
    private EventoMediaRepository mediaRepository;
    private EventoCompartidoRepository compartidoRepository;
    private ComentarioRepository comentarioRepository;
    private ValoracionRepository valoracionRepository;
    private EventoInteraccionService interaccionService;

    private Evento eventoEjemplo;
    private Usuario fernando;
    private Usuario gina;

    @BeforeEach
    void setUp() {
        mediaStorageService = new MediaStorageService(tempDir.toString());
        eventoRepository = mock(EventoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        mediaRepository = mock(EventoMediaRepository.class);
        compartidoRepository = mock(EventoCompartidoRepository.class);
        comentarioRepository = mock(ComentarioRepository.class);
        valoracionRepository = mock(ValoracionRepository.class);

        interaccionService = new EventoInteraccionService(
                eventoRepository,
                usuarioRepository,
                mediaRepository,
                compartidoRepository,
                comentarioRepository,
                valoracionRepository,
                mediaStorageService,
                "http://localhost:8080"
        );

        fernando = new Usuario("fernando", "hash", Set.of("USER"));
        fernando.setId(1L);
        gina = new Usuario("gina", "hash", Set.of("USER"));
        gina.setId(2L);

        eventoEjemplo = new Evento(
                1L, "Torneo Valorant Open Santiago", "Descripción", "Valorant", "Torneo",
                "Chile", "Santiago", "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0),
                "Santiago", "Dirección", null, "Gamer Chile Org", List.of(), List.of(),
                "Req", "Insc", List.of(), "/img/eventos/valorant-1.jpg",
                true, true, true, false, true, true, LocalDate.of(2026, 10, 20)
        );
        eventoEjemplo.setCreador(fernando);
    }

    @Test
    @DisplayName("MediaStorageService valida HTTPS y rechaza esquemas inseguros (XSS / Path Traversal)")
    void validarUrlSeguraYRechazarInseguras() {
        assertEquals("https://cdn.ejemplo.com/foto.jpg",
                mediaStorageService.validarYNormalizarUrl("  https://cdn.ejemplo.com/foto.jpg  "));
        assertEquals("/img/eventos/valorant-1.jpg",
                mediaStorageService.validarYNormalizarUrl("/img/eventos/valorant-1.jpg"));

        assertThrows(ResponseStatusException.class,
                () -> mediaStorageService.validarYNormalizarUrl("javascript:alert(1)"));
        assertThrows(ResponseStatusException.class,
                () -> mediaStorageService.validarYNormalizarUrl("http://sitio-inseguro.com/a.jpg"));
        assertThrows(ResponseStatusException.class,
                () -> mediaStorageService.validarYNormalizarUrl("/uploads/../../etc/passwd"));
    }

    @Test
    @DisplayName("MediaStorageService extrae URL embed segura de YouTube y Vimeo")
    void extraerEmbedUrlYoutubeYVimeo() {
        assertEquals("https://www.youtube-nocookie.com/embed/e_E9W2vsRbQ",
                mediaStorageService.extraerEmbedUrl("https://www.youtube.com/watch?v=e_E9W2vsRbQ", TipoMedia.VIDEO));
        assertEquals("https://www.youtube-nocookie.com/embed/e_E9W2vsRbQ",
                mediaStorageService.extraerEmbedUrl("https://youtu.be/e_E9W2vsRbQ", TipoMedia.VIDEO));
        assertEquals("https://player.vimeo.com/video/123456789",
                mediaStorageService.extraerEmbedUrl("https://vimeo.com/123456789", TipoMedia.VIDEO));
        assertNull(mediaStorageService.extraerEmbedUrl("https://cdn.ejemplo.com/clip.mp4", TipoMedia.VIDEO));
        assertNull(mediaStorageService.extraerEmbedUrl("https://www.youtube.com/watch?v=e_E9W2vsRbQ", TipoMedia.FOTO));
    }

    @Test
    @DisplayName("MediaStorageService acepta archivos permitidos y rechaza extensiones o MIME inválidos")
    void guardarArchivoValidaFormatoYRechazaPeligrosos() {
        MockMultipartFile fotoValida = new MockMultipartFile(
                "archivo", "arena.png", "image/png", new byte[]{1, 2, 3, 4});
        String ruta = mediaStorageService.guardarArchivo(fotoValida, TipoMedia.FOTO);
        assertTrue(ruta.startsWith("/uploads/"));
        assertTrue(ruta.endsWith(".png"));

        MockMultipartFile scriptMalicioso = new MockMultipartFile(
                "archivo", "shell.jsp", "application/octet-stream", "malicious".getBytes());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> mediaStorageService.guardarArchivo(scriptMalicioso, TipoMedia.FOTO));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        assertThrows(ResponseStatusException.class,
                () -> mediaStorageService.cargarArchivo("../secreto.png"));
    }

    @Test
    @DisplayName("Compartir evento persiste en BD y genera enlaces para sitio web y redes sociales")
    void compartirEventoGeneraEnlacesYGuarda() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoEjemplo));
        when(usuarioRepository.findByUsername("fernando")).thenReturn(Optional.of(fernando));
        when(compartidoRepository.save(any(EventoCompartido.class))).thenAnswer(inv -> {
            EventoCompartido c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        EventoCompartidoDTO dto = interaccionService.compartirEvento(
                1L, "fernando", new CompartirEventoRequest(CanalCompartido.WHATSAPP, "¡Vamos al torneo!"));

        assertEquals(10L, dto.getId());
        assertEquals("fernando", dto.getUsuario());
        assertEquals(CanalCompartido.WHATSAPP, dto.getCanal());
        assertTrue(dto.getEnlaceCompartido().startsWith("https://wa.me/?text="));
        assertTrue(dto.getEnlacesRedesSociales().containsKey("twitter"));
        assertTrue(dto.getEnlacesRedesSociales().containsKey("facebook"));
        assertTrue(dto.getEnlacesRedesSociales().containsKey("linkedin"));
        assertEquals("http://localhost:8080/eventos/1", dto.getEnlacesRedesSociales().get("sitioWeb"));
    }

    @Test
    @DisplayName("Eliminar comentario protege contra IDOR (solo autor o ADMIN pueden eliminar)")
    void eliminarComentarioProtegeContraIdor() {
        Comentario comentarioDeFernando = new Comentario(eventoEjemplo, fernando, "Buen evento");
        comentarioDeFernando.setId(5L);
        when(comentarioRepository.findById(5L)).thenReturn(Optional.of(comentarioDeFernando));

        // Otro usuario sin rol ADMIN recibe 403 Forbidden
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> interaccionService.eliminarComentario(1L, 5L, "gina", false));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(comentarioRepository, never()).delete(any());

        // El propio autor sí puede eliminar
        interaccionService.eliminarComentario(1L, 5L, "fernando", false);
        verify(comentarioRepository, times(1)).delete(comentarioDeFernando);
    }

    @Test
    @DisplayName("Valorar evento actualiza valoración existente del mismo usuario sin duplicar")
    void valorarEventoActualizaExistente() {
        Valoracion existente = new Valoracion(eventoEjemplo, gina, 3);
        existente.setId(7L);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoEjemplo));
        when(eventoRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.findByUsername("gina")).thenReturn(Optional.of(gina));
        when(valoracionRepository.findByEventoIdAndUsuarioId(1L, 2L)).thenReturn(Optional.of(existente));
        when(valoracionRepository.findByEventoIdOrderByFechaActualizacionDesc(1L)).thenReturn(List.of(existente));
        when(valoracionRepository.obtenerPromedioPorEventoId(1L)).thenReturn(5.0);

        ResumenValoracionesDTO resumen = interaccionService.valorarEvento(1L, "gina", new ValoracionRequest(5));

        assertEquals(5, existente.getPuntuacion());
        assertEquals(5.0, resumen.getPromedio());
        assertEquals(5, resumen.getMiValoracion());
        verify(valoracionRepository, times(1)).save(existente);
    }
}
