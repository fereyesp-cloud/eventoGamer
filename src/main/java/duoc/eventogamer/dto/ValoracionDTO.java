package duoc.eventogamer.dto;

import java.time.LocalDateTime;

public class ValoracionDTO {

    private Long id;
    private Long eventoId;
    private String usuario;
    private int puntuacion;
    private LocalDateTime fechaActualizacion;

    public ValoracionDTO() {}

    public ValoracionDTO(Long id, Long eventoId, String usuario, int puntuacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.eventoId = eventoId;
        this.usuario = usuario;
        this.puntuacion = puntuacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
