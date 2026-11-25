package duoc.eventogamer.service;

import duoc.eventogamer.dto.*;
import duoc.eventogamer.model.*;
import duoc.eventogamer.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventoInteraccionServiceTest {

    @TempDir
    Path tempDir;

    private EventoRepository eventoRepository;
    private UsuarioRepository usuarioRepository;
    private EventoMediaRepository mediaRepository;
    private EventoCompartidoRepository compartidoRepository;
    private ComentarioRepository comentarioRepository;
    private ValoracionRepository valoracionRepository;
    private EventoInteraccionService service;
    private Evento evento;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        eventoRepository = mock(EventoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        mediaRepository = mock(EventoMediaRepository.class);
        compartidoRepository = mock(EventoCompartidoRepository.class);
        comentarioRepository = mock(ComentarioRepository.class);
        valoracionRepository = mock(ValoracionRepository.class);
        service = new EventoInteraccionService(
                eventoRepository, usuarioRepository, mediaRepository, compartidoRepository,
                comentarioRepository, valoracionRepository, new MediaStorageService(tempDir.toString()),
                "http://localhost:8080"
        );

        usuario = new Usuario("fernando", "hash", Set.of("USER"));
        usuario.setId(1L);
        evento = new Evento(1L, "Torneo Valorant", "Desc", "Valorant", "Torneo",
                "Chile", "Santiago", "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0),
                "Santiago", "Dir", null, "Org", List.of(), List.of(), "Req", "Insc",
                List.of(), "/img/a.jpg", true, true, true, false, true, true, LocalDate.of(2026, 10, 20));
    }

    @Test
    @DisplayName("agregarMediaPorUrl y agregarComentario persisten correctamente en repositorios")
    void agregarMediaYComentario() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(usuarioRepository.findByUsername("fernando")).thenReturn(Optional.of(usuario));
        when(mediaRepository.save(any(EventoMedia.class))).thenAnswer(inv -> {
            EventoMedia m = inv.getArgument(0);
            m.setId(11L);
            return m;
        });
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(inv -> {
            Comentario c = inv.getArgument(0);
            c.setId(22L);
            return c;
        });

        EventoMediaDTO media = service.agregarMediaPorUrl(
                1L, "fernando", new EventoMediaRequest(TipoMedia.FOTO, "https://cdn.ejemplo.com/arena.jpg", "Arena"));
        ComentarioDTO comentario = service.agregarComentario(
                1L, "fernando", new ComentarioRequest("¡Muy buen torneo!"));

        assertEquals(11L, media.getId());
        assertEquals(TipoMedia.FOTO, media.getTipo());
        assertEquals(22L, comentario.getId());
        assertEquals("fernando", comentario.getUsuario());
    }
}
