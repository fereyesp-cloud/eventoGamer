package duoc.eventogamer.dto;

import duoc.eventogamer.model.TipoMedia;
import java.time.LocalDateTime;

public class EventoMediaDTO {

    private Long id;
    private Long eventoId;
    private String usuario;
    private TipoMedia tipo;
    private String url;
    private String embedUrl;
    private String titulo;
    private LocalDateTime fechaCreacion;

    public EventoMediaDTO() {}

    public EventoMediaDTO(Long id, Long eventoId, String usuario, TipoMedia tipo,
                          String url, String embedUrl, String titulo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.eventoId = eventoId;
        this.usuario = usuario;
        this.tipo = tipo;
        this.url = url;
        this.embedUrl = embedUrl;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public TipoMedia getTipo() { return tipo; }
    public void setTipo(TipoMedia tipo) { this.tipo = tipo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getEmbedUrl() { return embedUrl; }
    public void setEmbedUrl(String embedUrl) { this.embedUrl = embedUrl; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
