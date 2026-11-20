package duoc.eventogamer.dto;

import duoc.eventogamer.model.CanalCompartido;
import java.time.LocalDateTime;
import java.util.Map;

public class EventoCompartidoDTO {

    private Long id;
    private Long eventoId;
    private String eventoNombre;
    private String eventoVideojuego;
    private String usuario;
    private CanalCompartido canal;
    private String mensaje;
    private String enlaceCompartido;
    private LocalDateTime fechaCompartido;
    private Map<String, String> enlacesRedesSociales;

    public EventoCompartidoDTO() {}

    public EventoCompartidoDTO(Long id, Long eventoId, String eventoNombre, String eventoVideojuego,
                               String usuario, CanalCompartido canal, String mensaje,
                               String enlaceCompartido, LocalDateTime fechaCompartido,
                               Map<String, String> enlacesRedesSociales) {
        this.id = id;
        this.eventoId = eventoId;
        this.eventoNombre = eventoNombre;
        this.eventoVideojuego = eventoVideojuego;
        this.usuario = usuario;
        this.canal = canal;
        this.mensaje = mensaje;
        this.enlaceCompartido = enlaceCompartido;
        this.fechaCompartido = fechaCompartido;
        this.enlacesRedesSociales = enlacesRedesSociales;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getEventoNombre() { return eventoNombre; }
    public void setEventoNombre(String eventoNombre) { this.eventoNombre = eventoNombre; }

    public String getEventoVideojuego() { return eventoVideojuego; }
    public void setEventoVideojuego(String eventoVideojuego) { this.eventoVideojuego = eventoVideojuego; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public CanalCompartido getCanal() { return canal; }
    public void setCanal(CanalCompartido canal) { this.canal = canal; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getEnlaceCompartido() { return enlaceCompartido; }
    public void setEnlaceCompartido(String enlaceCompartido) { this.enlaceCompartido = enlaceCompartido; }

    public LocalDateTime getFechaCompartido() { return fechaCompartido; }
    public void setFechaCompartido(LocalDateTime fechaCompartido) { this.fechaCompartido = fechaCompartido; }

    public Map<String, String> getEnlacesRedesSociales() { return enlacesRedesSociales; }
    public void setEnlacesRedesSociales(Map<String, String> enlacesRedesSociales) { this.enlacesRedesSociales = enlacesRedesSociales; }
}
