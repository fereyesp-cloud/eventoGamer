package duoc.eventogamer.service;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import duoc.eventogamer.dto.InicioDTO;
import duoc.eventogamer.model.Banner;
import duoc.eventogamer.model.Evento;
import duoc.eventogamer.repository.BannerRepository;
import duoc.eventogamer.repository.EventoRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)   // todas las operaciones de este servicio son de solo lectura
public class EventoService {

    private final EventoRepository eventoRepository;
    private final BannerRepository bannerRepository;

    public EventoService(EventoRepository eventoRepository, BannerRepository bannerRepository) {
        this.eventoRepository = eventoRepository;
        this.bannerRepository = bannerRepository;
    }

    // ==========================================
    // API de Inicio [Pública]
    // ==========================================
    public InicioDTO obtenerDatosInicio() {
        return new InicioDTO(
                obtenerRecientes(),
                obtenerPopulares(),
                obtenerBanners(),
                "/api/auth/login"
        );
    }

    public List<EventoResumenDTO> obtenerRecientes() {
        return eventoRepository.findTop4ByOrderByFechaPublicacionDesc().stream()
                .map(Evento::toResumenDTO)
                .toList();
    }

    public List<EventoResumenDTO> obtenerPopulares() {
        return eventoRepository.findByPopularTrueOrderByFechaAsc().stream()
                .map(Evento::toResumenDTO)
                .toList();
    }

    public List<String> obtenerBanners() {
        return bannerRepository.findByActivoTrueOrderByOrdenAsc().stream()
                .map(Banner::getTexto)
                .toList();
    }

    // ==========================================
    // Búsqueda y Filtros [Pública]
    // ==========================================
    public List<EventoResumenDTO> buscarAvanzado(String nombre,
                                                 String videojuego,
                                                 String categoria,
                                                 String paisOCiudad,
                                                 LocalDate fecha,
                                                 String modalidad) {
        return eventoRepository.buscarAvanzado(
                        normalizar(nombre),
                        normalizar(videojuego),
                        normalizar(categoria),
                        normalizar(paisOCiudad),
                        fecha,
                        normalizar(modalidad))
                .stream()
                .map(Evento::toResumenDTO)
                .toList();
    }

    // ==========================================
    // Detalle de Evento [Privada con JWT]
    // ==========================================
    public Optional<EventoDetalleDTO> obtenerDetalle(Long id) {
        // La conversión a DTO ocurre DENTRO de la transacción,
        // así las listas lazy (fotografías, organizadores) se pueden cargar
        return eventoRepository.findById(id).map(Evento::toDetalleDTO);
    }

    public Optional<Evento> buscarPorId(Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        // La vista web usa estas listas después de salir del servicio:
        // se cargan aquí para evitar LazyInitializationException
        evento.ifPresent(e -> {
            Hibernate.initialize(e.getOrganizadores());
            Hibernate.initialize(e.getExpositores());
            Hibernate.initialize(e.getFotografias());
        });
        return evento;
    }

    // ==========================================
    // Compatibilidad con vistas Web
    // ==========================================
    public List<Evento> listarTodos() {
        return eventoRepository.findAllByOrderByFechaAscHoraAsc();
    }

    public List<Evento> proximos() {
        return eventoRepository.findByFechaGreaterThanEqualOrderByFechaAscHoraAsc(LocalDate.now());
    }

    public List<Evento> destacados() {
        return eventoRepository.findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now());
    }

    public List<Evento> buscar(String categoria, LocalDate fecha, String ubicacion) {
        return eventoRepository.buscarAvanzado(
                null, null, normalizar(categoria), normalizar(ubicacion), fecha, null);
    }

    public List<String> categorias() {
        return eventoRepository.listarCategorias();
    }

    public List<String> videojuegos() {
        return eventoRepository.listarVideojuegos();
    }

    public List<String> modalidades() {
        return List.of("Presencial", "Online");
    }

    public List<String> ubicaciones() {
        return eventoRepository.listarCiudades();
    }

    private String normalizar(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}