package duoc.eventogamer.dto;

import java.time.LocalDateTime;

public class ComentarioDTO {

    private Long id;
    private Long eventoId;
    private String usuario;
    private String contenido;
    private LocalDateTime fechaCreacion;

    public ComentarioDTO() {}

    public ComentarioDTO(Long id, Long eventoId, String usuario, String contenido, LocalDateTime fechaCreacion) {
        this.id = id;
        this.eventoId = eventoId;
        this.usuario = usuario;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
