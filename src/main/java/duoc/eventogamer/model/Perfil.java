package duoc.eventogamer.model;

import java.util.ArrayList;
import java.util.List;

public class Perfil {

    private final String username;
    private String nombre;
    private String correo;
    private String gamertag;
    private String avatar = "🎮";
    private List<String> juegos = new ArrayList<>();
    private boolean notifNuevosEventos = true;
    private boolean notifRecordatorios = true;
    private boolean notifCorreo = false;

    public Perfil(String username) {
        this.username = username;
        this.gamertag = username;
    }

    public String getUsername() { return username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

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