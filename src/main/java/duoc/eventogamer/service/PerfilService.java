package duoc.eventogamer.service;

import duoc.eventogamer.model.Perfil;
import duoc.eventogamer.model.PerfilForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PerfilService {

    // Listas cerradas de opciones válidas: lo que llegue fuera de ellas se rechaza
    public static final List<String> AVATARES = List.of("🎮", "👾", "🤖", "🐉", "🥷", "🧙");
    public static final List<String> JUEGOS = List.of(
        "Valorant", "League of Legends", "Fortnite", "Minecraft",
        "Counter-Strike 2", "EA Sports FC", "Street Fighter 6", "Rocket League"
    );

    // ConcurrentHashMap: seguro si varios usuarios guardan al mismo tiempo
    private final Map<String, Perfil> perfiles = new ConcurrentHashMap<>();

    // Llamado desde el registro para guardar nombre y correo
    public void crear(String username, String nombre, String correo) {
        Perfil perfil = new Perfil(username);
        perfil.setNombre(nombre);
        perfil.setCorreo(correo);
        perfiles.put(username, perfil);
    }

    // Si el perfil no existe (por ejemplo, los 3 usuarios iniciales), lo crea vacío
    public Perfil obtener(String username) {
        return perfiles.computeIfAbsent(username, Perfil::new);
    }

    // Copia SOLO los campos editables del formulario
    public void actualizar(String username, PerfilForm form) {
        Perfil perfil = obtener(username);
        perfil.setGamertag(form.getGamertag());
        perfil.setAvatar(form.getAvatar());
        perfil.setJuegos(new ArrayList<>(form.getJuegos()));
        perfil.setNotifNuevosEventos(form.isNotifNuevosEventos());
        perfil.setNotifRecordatorios(form.isNotifRecordatorios());
        perfil.setNotifCorreo(form.isNotifCorreo());
    }
}