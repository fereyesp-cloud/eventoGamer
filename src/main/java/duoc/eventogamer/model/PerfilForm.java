package duoc.eventogamer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

public class PerfilForm {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$",
             message = "Debe tener entre 3 y 20 caracteres: letras, números o guion bajo")
    private String gamertag;

    @NotBlank(message = "Selecciona un avatar")
    private String avatar;

    private List<String> juegos = new ArrayList<>();
    private boolean notifNuevosEventos;
    private boolean notifRecordatorios;
    private boolean notifCorreo;

    // Crea el formulario precargado con los datos actuales del perfil
    public static PerfilForm desde(Perfil p) {
        PerfilForm f = new PerfilForm();
        f.gamertag = p.getGamertag();
        f.avatar = p.getAvatar();
        f.juegos = new ArrayList<>(p.getJuegos());
        f.notifNuevosEventos = p.isNotifNuevosEventos();
        f.notifRecordatorios = p.isNotifRecordatorios();
        f.notifCorreo = p.isNotifCorreo();
        return f;
    }

    public String getGamertag() { return gamertag; }
    public void setGamertag(String gamertag) { this.gamertag = gamertag; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public List<String> getJuegos() { return juegos; }
    public void setJuegos(List<String> juegos) { this.juegos = juegos; }

    public boolean isNotifNuevosEventos() { return notifNuevosEventos; }
    public void setNotifNuevosEventos(boolean v) { this.notifNuevosEventos = v; }

    public boolean isNotifRecordatorios() { return notifRecordatorios; }
    public void setNotifRecordatorios(boolean v) { this.notifRecordatorios = v; }

    public boolean isNotifCorreo() { return notifCorreo; }
    public void setNotifCorreo(boolean v) { this.notifCorreo = v; }
}