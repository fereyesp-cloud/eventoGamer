package duoc.eventogamer.service;

import duoc.eventogamer.model.Evento;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final List<Evento> eventos = List.of(
        new Evento(1L, "Torneo Valorant Santiago",
            "Torneo 5v5 abierto con premios en efectivo y transmisión en vivo.",
            "Torneo", LocalDate.of(2026, 11, 14), LocalTime.of(10, 0),
            "Santiago", "Av. Libertador Bernardo O'Higgins 3349, Estación Central",
            List.of("Gamer Chile Org"), List.of("Equipo pro invitado", "Caster oficial"),
            true, true, true, false, true),
        new Evento(2L, "Retro Gaming Fest",
            "Exhibición de consolas clásicas, free play y concurso de speedrun.",
            "Exposición", LocalDate.of(2026, 11, 28), LocalTime.of(12, 0),
            "Valparaíso", "Av. Brasil 1786, Valparaíso",
            List.of("Club Retro Valpo"), List.of("Coleccionistas nacionales"),
            false, true, true, false, false),
        new Evento(3L, "LAN Party Concepción",
            "48 horas de juego en red. Trae tu PC.",
            "LAN Party", LocalDate.of(2026, 12, 5), LocalTime.of(18, 0),
            "Concepción", "Calle Barros Arana 1068, Concepción",
            List.of("LAN Sur"), List.of("Streamers locales"),
            true, true, true, true, true),
        new Evento(4L, "Meetup Desarrolladores Indie",
            "Charlas y demos jugables de estudios independientes chilenos.",
            "Meetup", LocalDate.of(2026, 12, 12), LocalTime.of(16, 30),
            "Santiago", "Av. Vicuña Mackenna 4860, Macul",
            List.of("Comunidad Indie CL", "VG Chile"), List.of("Estudios indie invitados"),
            true, true, false, false, false)
    );

    public List<Evento> listarTodos() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getFecha))
                .toList();
    }

    public Optional<Evento> buscarPorId(Long id) {
        return eventos.stream().filter(e -> e.getId().equals(id)).findFirst();
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
        return listarTodos().stream()
                .filter(e -> categoria == null || categoria.isBlank() || e.getCategoria().equalsIgnoreCase(categoria))
                .filter(e -> fecha == null || e.getFecha().equals(fecha))
                .filter(e -> ubicacion == null || ubicacion.isBlank() || e.getUbicacion().equalsIgnoreCase(ubicacion))
                .toList();
    }

    public List<String> categorias() {
        return eventos.stream().map(Evento::getCategoria).distinct().sorted().toList();
    }

    public List<String> ubicaciones() {
        return eventos.stream().map(Evento::getUbicacion).distinct().sorted().toList();
    }
}