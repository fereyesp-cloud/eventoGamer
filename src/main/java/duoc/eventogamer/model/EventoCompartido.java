package duoc.eventogamer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_compartido", indexes = {
    @Index(name = "idx_compartido_evento", columnList = "evento_id"),
    @Index(name = "idx_compartido_canal_fecha", columnList = "canal, fecha_compartido")
})
public class EventoCompartido {

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
    @Column(nullable = false, length = 20)
    private CanalCompartido canal;

    @Column(length = 300)
    private String mensaje;

    @Column(name = "enlace_compartido", nullable = false, length = 500)
    private String enlaceCompartido;

    @Column(name = "fecha_compartido", nullable = false)
    private LocalDateTime fechaCompartido;

    public EventoCompartido() {}

    public EventoCompartido(Evento evento, Usuario usuario, CanalCompartido canal,
                            String mensaje, String enlaceCompartido) {
        this.evento = evento;
        this.usuario = usuario;
        this.canal = canal;
        this.mensaje = mensaje;
        this.enlaceCompartido = enlaceCompartido;
        this.fechaCompartido = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public CanalCompartido getCanal() { return canal; }
    public void setCanal(CanalCompartido canal) { this.canal = canal; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getEnlaceCompartido() { return enlaceCompartido; }
    public void setEnlaceCompartido(String enlaceCompartido) { this.enlaceCompartido = enlaceCompartido; }

    public LocalDateTime getFechaCompartido() { return fechaCompartido; }
    public void setFechaCompartido(LocalDateTime fechaCompartido) { this.fechaCompartido = fechaCompartido; }
}
