package duoc.eventogamer.service;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.InicioDTO;
import duoc.eventogamer.model.Banner;
import duoc.eventogamer.model.Evento;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.BannerRepository;
import duoc.eventogamer.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventoServiceTest {

    private EventoRepository eventoRepository;
    private BannerRepository bannerRepository;
    private EventoInteraccionService interaccionService;
    private EventoService eventoService;
    private Evento evento;

    @BeforeEach
    void setUp() {
        eventoRepository = mock(EventoRepository.class);
        bannerRepository = mock(BannerRepository.class);
        interaccionService = mock(EventoInteraccionService.class);
        eventoService = new EventoService(eventoRepository, bannerRepository, interaccionService);

        evento = new Evento(1L, "Torneo Valorant", "Desc", "Valorant", "Torneo",
                "Chile", "Santiago", "Presencial", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0),
                "Santiago", "Av. O'Higgins 3349", null, "Gamer Chile", List.of("Gamer Chile"),
                List.of("Caster"), "Req", "Insc", List.of("/img/a.jpg"), "/img/a.jpg",
                true, true, true, false, true, true, LocalDate.of(2026, 10, 20));
        evento.setCreador(new Usuario("fernando", "hash", Set.of("USER")));
    }

    @Test
    @DisplayName("obtenerDatosInicio retorna recientes, populares, banners y loginUrl")
    void obtenerDatosInicio() {
        when(eventoRepository.findTop4ByOrderByFechaPublicacionDesc()).thenReturn(List.of(evento));
        when(eventoRepository.findByPopularTrueOrderByFechaAsc()).thenReturn(List.of(evento));
        when(bannerRepository.findByActivoTrueOrderByOrdenAsc()).thenReturn(List.of(new Banner("Promo Gamer", 1)));

        InicioDTO inicio = eventoService.obtenerDatosInicio();

        assertEquals(1, inicio.getEventosRecientes().size());
        assertEquals(1, inicio.getEventosPopulares().size());
        assertEquals(List.of("Promo Gamer"), inicio.getBannersAvisosComerciales());
        assertEquals("/api/auth/login", inicio.getLoginUrl());
    }

    @Test
    @DisplayName("obtenerDetalle enriquece EventoDetalleDTO con creador, multimedia y métricas")
    void obtenerDetalleEnriquecido() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(interaccionService.listarMedia(1L)).thenReturn(List.of());
        when(interaccionService.obtenerPromedioValoracion(1L)).thenReturn(4.5);
        when(interaccionService.contarValoraciones(1L)).thenReturn(2L);
        when(interaccionService.contarComentarios(1L)).thenReturn(3L);
        when(interaccionService.contarCompartidos(1L)).thenReturn(4L);

        EventoDetalleDTO dto = eventoService.obtenerDetalle(1L).orElseThrow();

        assertEquals("fernando", dto.getPublicadoPor());
        assertEquals(4.5, dto.getPromedioValoracion());
        assertEquals(2L, dto.getTotalValoraciones());
        assertEquals(3L, dto.getTotalComentarios());
        assertEquals(4L, dto.getTotalCompartidos());
    }
}
