package duoc.eventogamer.service;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import duoc.eventogamer.dto.InicioDTO;
import duoc.eventogamer.model.Evento;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    // Lista de banners de avisos comerciales asociados a los gamers
    public static final List<String> BANNERS_COMERCIALES = List.of(
            "HyperGear - 20% OFF en periféricos mecánicos con el cupón GAMER2026",
            "NovaChair - Sillas ergonómicas Pro Series para torneos oficiales",
            "PixelNet Fibra - Conexión gamer ultra baja latencia 1 Gbps simétrico",
            "EnergyDrink Monster - Patrocinador oficial del campeonato regional"
    );

    // Eventos iniciales con los campos completos requeridos por la rúbrica
    private final List<Evento> eventos = new ArrayList<>(List.of(
            new Evento(
                    1L,
                    "Torneo Valorant Open Santiago",
                    "Torneo 5v5 abierto de Valorant con premios en efectivo y transmisión en vivo por Twitch oficial.",
                    "Valorant",
                    "Torneo",
                    "Chile",
                    "Santiago",
                    "Presencial",
                    LocalDate.of(2026, 11, 14),
                    LocalTime.of(10, 0),
                    "Santiago",
                    "Av. Libertador Bernardo O'Higgins 3349, Estación Central",
                    null,
                    "Gamer Chile Org",
                    List.of("Gamer Chile Org", "Riot Community CL"),
                    List.of("KRÜ Esports Academy", "Caster Nacional"),
                    "Mayor de 16 años, rango mínimo Platino en temporada vigente, periféricos propios permitidos.",
                    "Inscripciones abiertas hasta el 10 de Noviembre. Cupos limitados a 16 escuadras.",
                    List.of("/img/eventos/valorant-1.jpg", "/img/eventos/valorant-arena.jpg"),
                    "/img/eventos/valorant-1.jpg",
                    true, true, true, false,
                    true, true, LocalDate.of(2026, 10, 20)
            ),
            new Evento(
                    2L,
                    "Copa League of Legends Online",
                    "Competencia clasificatoria 5v5 en Grieta del Invocador con casters oficiales y soporte vía Discord.",
                    "League of Legends",
                    "Torneo",
                    "Chile",
                    "Online",
                    "Online",
                    LocalDate.of(2026, 11, 20),
                    LocalTime.of(18, 0),
                    "Online",
                    "Servidor de Discord Oficial",
                    "https://discord.gg/lol-chile-cup",
                    "Liga Gamer Online",
                    List.of("Liga Gamer Online"),
                    List.of("Streamers invitados"),
                    "Residencia en Latinoamérica Sur (LAS), cuenta nivel 30+ sin sanciones de conducta.",
                    "Inscripción gratuita mediante formulario oficial. Cierre 2 días antes de la primera ronda.",
                    List.of("/img/eventos/lol-online.jpg"),
                    "/img/eventos/lol-online.jpg",
                    false, false, false, false,
                    true, true, LocalDate.of(2026, 10, 22)
            ),
            new Evento(
                    3L,
                    "Retro Gaming Fest Valparaíso",
                    "Exhibición de consolas clásicas, free play, zona arcade y torneo relámpago de Street Fighter.",
                    "Street Fighter 6",
                    "Exposición",
                    "Chile",
                    "Valparaíso",
                    "Presencial",
                    LocalDate.of(2026, 11, 28),
                    LocalTime.of(12, 0),
                    "Valparaíso",
                    "Av. Brasil 1786, Valparaíso",
                    null,
                    "Club Retro Valpo",
                    List.of("Club Retro Valpo"),
                    List.of("Coleccionistas Nacionales", "Comunidad Arcade"),
                    "Entrada liberada para todo público. Inscripción in situ para torneo relámpago.",
                    "Adhesión voluntaria en puerta. Cupos de torneo por orden de llegada.",
                    List.of("/img/eventos/retro-fest.jpg"),
                    "/img/eventos/retro-fest.jpg",
                    false, true, true, false,
                    false, false, LocalDate.of(2026, 10, 15)
            ),
            new Evento(
                    4L,
                    "LAN Party Concepción 48 Horas",
                    "Dos días seguidos de juego ininterrumpido en red local. Trae tu equipo (BYOC) o arrienda estación.",
                    "Counter-Strike 2",
                    "LAN Party",
                    "Chile",
                    "Concepción",
                    "Presencial",
                    LocalDate.of(2026, 12, 5),
                    LocalTime.of(18, 0),
                    "Concepción",
                    "Calle Barros Arana 1068, Concepción",
                    null,
                    "LAN Sur Producciones",
                    List.of("LAN Sur Producciones"),
                    List.of("Streamers locales", "Creadores de contenido"),
                    "Ser mayor de 18 años o contar con autorización notarial. Pase de movilidad al día.",
                    "Ticket participante BYOC $15.000 incluye puesto de red y credencial personalizada.",
                    List.of("/img/eventos/lan-party.jpg"),
                    "/img/eventos/lan-party.jpg",
                    true, true, true, true,
                    true, true, LocalDate.of(2026, 10, 25)
            ),
            new Evento(
                    5L,
                    "Meetup Desarrolladores Indie Games",
                    "Encuentro con estudios chilenos, muestras jugables de prototipos y paneles sobre motores Unreal y Unity.",
                    "Varios / Indie",
                    "Meetup",
                    "Chile",
                    "Santiago",
                    "Presencial",
                    LocalDate.of(2026, 12, 12),
                    LocalTime.of(16, 30),
                    "Santiago",
                    "Av. Vicuña Mackenna 4860, Macul",
                    null,
                    "VG Chile",
                    List.of("Comunidad Indie CL", "VG Chile"),
                    List.of("Estudios Indie invitados", "Profesores de desarrollo"),
                    "Abierto a estudiantes, desarrolladores y público general entusiasta de la industria.",
                    "Registro previo por Passline para control de aforo.",
                    List.of("/img/eventos/indie-meetup.jpg"),
                    "/img/eventos/indie-meetup.jpg",
                    true, true, false, false,
                    false, false, LocalDate.of(2026, 10, 18)
            )
    ));

    // ==========================================
    // Métodos para la API de Inicio [Pública]
    // ==========================================
    public InicioDTO obtenerDatosInicio() {
        return new InicioDTO(
                obtenerRecientes(),
                obtenerPopulares(),
                BANNERS_COMERCIALES,
                "/api/auth/login"
        );
    }

    public List<EventoResumenDTO> obtenerRecientes() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getFechaPublicacion, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(4)
                .map(Evento::toResumenDTO)
                .toList();
    }

    public List<EventoResumenDTO> obtenerPopulares() {
        return eventos.stream()
                .filter(Evento::isPopular)
                .sorted(Comparator.comparing(Evento::getFecha))
                .map(Evento::toResumenDTO)
                .toList();
    }

    public List<String> obtenerBanners() {
        return BANNERS_COMERCIALES;
    }

    // ==========================================
    // Búsqueda y Filtros [Pública]
    // Filtros: nombre, videojuego, tipo/categoria, pais/ciudad, fecha, modalidad
    // ==========================================
    public List<EventoResumenDTO> buscarAvanzado(String nombre,
                                                 String videojuego,
                                                 String categoria,
                                                 String paisOCiudad,
                                                 LocalDate fecha,
                                                 String modalidad) {

        return eventos.stream()
                .filter(e -> nombre == null || nombre.isBlank()
                        || e.getNombre().toLowerCase().contains(nombre.trim().toLowerCase()))
                .filter(e -> videojuego == null || videojuego.isBlank()
                        || (e.getVideojuego() != null && e.getVideojuego().toLowerCase().contains(videojuego.trim().toLowerCase())))
                .filter(e -> categoria == null || categoria.isBlank()
                        || (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(categoria.trim())))
                .filter(e -> {
                    if (paisOCiudad == null || paisOCiudad.isBlank()) return true;
                    String term = paisOCiudad.trim().toLowerCase();
                    boolean matchPais = e.getPais() != null && e.getPais().toLowerCase().contains(term);
                    boolean matchCiudad = e.getCiudad() != null && e.getCiudad().toLowerCase().contains(term);
                    boolean matchUbi = e.getUbicacion() != null && e.getUbicacion().toLowerCase().contains(term);
                    return matchPais || matchCiudad || matchUbi;
                })
                .filter(e -> fecha == null || (e.getFecha() != null && e.getFecha().equals(fecha)))
                .filter(e -> modalidad == null || modalidad.isBlank()
                        || (e.getModalidad() != null && e.getModalidad().equalsIgnoreCase(modalidad.trim())))
                .sorted(Comparator.comparing(Evento::getFecha))
                .map(Evento::toResumenDTO)
                .toList();
    }

    // ==========================================
    // Detalle de Evento [Privada con JWT]
    // ==========================================
    public Optional<EventoDetalleDTO> obtenerDetalle(Long id) {
        return eventos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(Evento::toDetalleDTO);
    }

    public Optional<Evento> buscarPorId(Long id) {
        return eventos.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    // ==========================================
    // Métodos de compatibilidad con vistas Web
    // ==========================================
    public List<Evento> listarTodos() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getFecha))
                .toList();
    }

    public List<Evento> proximos() {
        LocalDate hoy = LocalDate.now();
        return listarTodos().stream()
                .filter(e -> !e.getFecha().isBefore(hoy))
                .toList();
    }

    public List<Evento> destacados() {
        return proximos().stream().filter(Evento::isDestacado).toList();
    }

    public List<Evento> buscar(String categoria, LocalDate fecha, String ubicacion) {
        return eventos.stream()
                .filter(e -> categoria == null || categoria.isBlank() || e.getCategoria().equalsIgnoreCase(categoria))
                .filter(e -> fecha == null || e.getFecha().equals(fecha))
                .filter(e -> ubicacion == null || ubicacion.isBlank() || e.getUbicacion().equalsIgnoreCase(ubicacion))
                .sorted(Comparator.comparing(Evento::getFecha))
                .toList();
    }

    public List<String> categorias() {
        return eventos.stream().map(Evento::getCategoria).distinct().sorted().toList();
    }

    public List<String> videojuegos() {
        return eventos.stream().map(Evento::getVideojuego).distinct().sorted().toList();
    }

    public List<String> modalidades() {
        return List.of("Presencial", "Online");
    }

    public List<String> ubicaciones() {
        return eventos.stream().map(Evento::getCiudad).distinct().sorted().toList();
    }
}
