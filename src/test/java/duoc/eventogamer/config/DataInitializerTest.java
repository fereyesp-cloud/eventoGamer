package duoc.eventogamer.config;

import duoc.eventogamer.model.Evento;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Test
    @DisplayName("run carga usuarios, banners, eventos e interacciones cuando las tablas están vacías")
    void runCargaDatosInicialesCuandoEstaVacio() {
        EventoRepository eventoRepo = mock(EventoRepository.class);
        BannerRepository bannerRepo = mock(BannerRepository.class);
        UsuarioRepository usuarioRepo = mock(UsuarioRepository.class);
        EventoMediaRepository mediaRepo = mock(EventoMediaRepository.class);
        EventoCompartidoRepository compartidoRepo = mock(EventoCompartidoRepository.class);
        ComentarioRepository comentarioRepo = mock(ComentarioRepository.class);
        ValoracionRepository valoracionRepo = mock(ValoracionRepository.class);

        Usuario fernando = new Usuario("fernando", "h", Set.of("USER"));
        Usuario gina = new Usuario("gina", "h", Set.of("USER"));
        Usuario admin = new Usuario("admin", "h", Set.of("USER", "ADMIN"));
        Evento evento = new Evento(1L, "Torneo", "Desc", "Valorant", "Torneo", "Chile", "Santiago",
                "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0), "Santiago", "Dir", null,
                "Org", List.of(), List.of(), "Req", "Insc", List.of(), "/img/a.jpg",
                true, true, true, false, true, true, LocalDate.of(2026, 10, 20));
        evento.setCreador(fernando);

        when(usuarioRepo.count()).thenReturn(0L);
        when(bannerRepo.count()).thenReturn(0L);
        when(eventoRepo.count()).thenReturn(0L);
        when(mediaRepo.count()).thenReturn(0L);
        when(comentarioRepo.count()).thenReturn(0L);
        when(valoracionRepo.count()).thenReturn(0L);
        when(compartidoRepo.count()).thenReturn(0L);
        when(usuarioRepo.findByUsername("fernando")).thenReturn(Optional.of(fernando));
        when(usuarioRepo.findByUsername("gina")).thenReturn(Optional.of(gina));
        when(usuarioRepo.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(eventoRepo.findAll()).thenReturn(List.of(evento));
        when(eventoRepo.findAllByOrderByFechaAscHoraAsc()).thenReturn(List.of(evento));

        DataInitializer initializer = new DataInitializer(
                eventoRepo, bannerRepo, usuarioRepo, mediaRepo, compartidoRepo,
                comentarioRepo, valoracionRepo, new BCryptPasswordEncoder(),
                "UserSeedPass2026!", "AdminSeedPass2026!", "http://localhost:8080"
        );

        initializer.run();

        verify(usuarioRepo, times(1)).saveAll(anyIterable());
        verify(bannerRepo, times(1)).saveAll(anyIterable());
        verify(eventoRepo, times(1)).saveAll(anyIterable());
        verify(mediaRepo, times(1)).saveAll(anyIterable());
        verify(comentarioRepo, times(1)).saveAll(anyIterable());
        verify(valoracionRepo, times(1)).saveAll(anyIterable());
        verify(compartidoRepo, times(1)).saveAll(anyIterable());
    }
}
