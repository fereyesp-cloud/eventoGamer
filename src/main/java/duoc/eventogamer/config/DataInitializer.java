package duoc.eventogamer.config;

import duoc.eventogamer.model.Banner;
import duoc.eventogamer.model.Evento;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.BannerRepository;
import duoc.eventogamer.repository.EventoRepository;
import duoc.eventogamer.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final EventoRepository eventoRepository;
    private final BannerRepository bannerRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String passwordUsuarios;
    private final String passwordAdmin;

    public DataInitializer(EventoRepository eventoRepository,
                           BannerRepository bannerRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.seed.user-password}") String passwordUsuarios,
                           @Value("${app.seed.admin-password}") String passwordAdmin) {
        this.eventoRepository = eventoRepository;
        this.bannerRepository = bannerRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordUsuarios = passwordUsuarios;
        this.passwordAdmin = passwordAdmin;
    }

    @Override
    @Transactional
    public void run(String... args) {
        cargarUsuarios();
        cargarBanners();
        cargarEventos();
    }

    // Solo inserta si la tabla está vacía: reiniciar la app no duplica datos
    private void cargarUsuarios() {
        if (usuarioRepository.count() > 0) return;

        usuarioRepository.saveAll(List.of(
            new Usuario("fernando", passwordEncoder.encode(passwordUsuarios), Set.of("USER")),
            new Usuario("gina", passwordEncoder.encode(passwordUsuarios), Set.of("USER")),
            new Usuario("admin", passwordEncoder.encode(passwordAdmin), Set.of("USER", "ADMIN"))
        ));
        log.info("Usuarios iniciales cargados");   // nunca registrar contraseñas en el log
    }

    private void cargarBanners() {
        if (bannerRepository.count() > 0) return;

        bannerRepository.saveAll(List.of(
            new Banner("HyperGear - 20% OFF en periféricos mecánicos con el cupón GAMER2026", 1),
            new Banner("NovaChair - Sillas ergonómicas Pro Series para torneos oficiales", 2),
            new Banner("PixelNet Fibra - Conexión gamer ultra baja latencia 1 Gbps simétrico", 3),
            new Banner("EnergyDrink Monster - Patrocinador oficial del campeonato regional", 4)
        ));
        log.info("Banners iniciales cargados");
    }

        private void cargarEventos() {
        if (eventoRepository.count() > 0) return;

        eventoRepository.saveAll(List.of(
            new Evento(
                    null,
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
                    null,
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
                    null,
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
                    null,
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
                    null,
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
        log.info("Eventos iniciales cargados");
    }
}