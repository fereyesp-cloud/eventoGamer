package duoc.eventogamer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_media", indexes = {
    @Index(name = "idx_media_evento", columnList = "evento_id"),
    @Index(name = "idx_media_usuario", columnList = "usuario_id")
})
public class EventoMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMedia tipo;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 120)
    private String titulo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public EventoMedia() {}

    public EventoMedia(Evento evento, Usuario usuario, TipoMedia tipo, String url, String titulo) {
        this.evento = evento;
        this.usuario = usuario;
        this.tipo = tipo;
        this.url = url;
        this.titulo = titulo;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public TipoMedia getTipo() { return tipo; }
    public void setTipo(TipoMedia tipo) { this.tipo = tipo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
