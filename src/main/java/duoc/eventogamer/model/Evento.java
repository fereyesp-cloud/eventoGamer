package duoc.eventogamer.model;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento", indexes = {
    @Index(name = "idx_evento_fecha", columnList = "fecha"),
    @Index(name = "idx_evento_ubicacion", columnList = "pais, ciudad"),
    @Index(name = "idx_evento_modalidad", columnList = "modalidad"),
    @Index(name = "idx_evento_publicacion", columnList = "fecha_publicacion")
})
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String videojuego;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(length = 60)
    private String pais;

    @Column(length = 80)
    private String ciudad;

    @Column(nullable = false, length = 20)
    private String modalidad; // "Presencial" u "Online"

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(length = 150)
    private String ubicacion;

    @Column(length = 200)
    private String direccion;

    @Column(name = "enlace_acceso", length = 300)
    private String enlaceAcceso;

    @Column(length = 120)
    private String organizador;

    @ElementCollection
    @CollectionTable(name = "evento_organizador", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "nombre", length = 120)
    private List<String> organizadores = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "evento_expositor", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "nombre", length = 120)
    private List<String> expositores = new ArrayList<>();

    @Column(name = "requisitos_participacion", columnDefinition = "TEXT")
    private String requisitosParticipacion;

    @Column(name = "informacion_inscripcion", columnDefinition = "TEXT")
    private String informacionInscripcion;

    @ElementCollection
    @CollectionTable(name = "evento_foto", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "url", length = 300)
    private List<String> fotografias = new ArrayList<>();

    @Column(name = "foto_principal", length = 300)
    private String fotoPrincipal;

    private boolean estacionamiento;

    @Column(name = "locomocion_colectiva")
    private boolean locomocionColectiva;

    @Column(name = "servicio_comida")
    private boolean servicioComida;

    private boolean alojamiento;
    private boolean destacado;
    private boolean popular;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion;

    public Evento() {}

    public Evento(Long id, String nombre, String descripcion, String videojuego,
                  String categoria, String pais, String ciudad, String modalidad,
                  LocalDate fecha, LocalTime hora, String ubicacion, String direccion,
                  String enlaceAcceso, String organizador, List<String> organizadores,
                  List<String> expositores, String requisitosParticipacion,
                  String informacionInscripcion, List<String> fotografias, String fotoPrincipal,
                  boolean estacionamiento, boolean locomocionColectiva,
                  boolean servicioComida, boolean alojamiento,
                  boolean destacado, boolean popular, LocalDate fechaPublicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.videojuego = videojuego;
        this.categoria = categoria;
        this.pais = pais;
        this.ciudad = ciudad;
        this.modalidad = modalidad;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
        this.direccion = direccion;
        this.enlaceAcceso = enlaceAcceso;
        this.organizador = organizador;
        this.organizadores = organizadores != null ? organizadores : new ArrayList<>();
        this.expositores = expositores != null ? expositores : new ArrayList<>();
        this.requisitosParticipacion = requisitosParticipacion;
        this.informacionInscripcion = informacionInscripcion;
        this.fotografias = fotografias != null ? fotografias : new ArrayList<>();
        this.fotoPrincipal = fotoPrincipal;
        this.estacionamiento = estacionamiento;
        this.locomocionColectiva = locomocionColectiva;
        this.servicioComida = servicioComida;
        this.alojamiento = alojamiento;
        this.destacado = destacado;
        this.popular = popular;
        this.fechaPublicacion = fechaPublicacion;
    }

    public EventoResumenDTO toResumenDTO() {
        return new EventoResumenDTO(
                id,
                nombre,
                videojuego,
                categoria,
                pais,
                ciudad,
                modalidad,
                fecha,
                hora,
                fotoPrincipal,
                popular
        );
    }

    public EventoDetalleDTO toDetalleDTO() {
        return new EventoDetalleDTO(
                id,
                nombre,
                descripcion,
                videojuego,
                categoria,
                pais,
                ciudad,
                modalidad,
                fecha,
                hora,
                direccion != null && !direccion.isBlank() ? direccion : ubicacion,
                enlaceAcceso,
                organizador != null ? organizador : (organizadores.isEmpty() ? "Comunidad Gamer" : String.join(", ", organizadores)),
                requisitosParticipacion,
                informacionInscripcion,
                fotografias,
                popular
        );
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getVideojuego() { return videojuego; }
    public void setVideojuego(String videojuego) { this.videojuego = videojuego; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEnlaceAcceso() { return enlaceAcceso; }
    public void setEnlaceAcceso(String enlaceAcceso) { this.enlaceAcceso = enlaceAcceso; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public List<String> getOrganizadores() { return organizadores; }
    public void setOrganizadores(List<String> organizadores) { this.organizadores = organizadores; }

    public List<String> getExpositores() { return expositores; }
    public void setExpositores(List<String> expositores) { this.expositores = expositores; }

    public String getRequisitosParticipacion() { return requisitosParticipacion; }
    public void setRequisitosParticipacion(String requisitosParticipacion) { this.requisitosParticipacion = requisitosParticipacion; }

    public String getInformacionInscripcion() { return informacionInscripcion; }
    public void setInformacionInscripcion(String informacionInscripcion) { this.informacionInscripcion = informacionInscripcion; }

    public List<String> getFotografias() { return fotografias; }
    public void setFotografias(List<String> fotografias) { this.fotografias = fotografias; }

    public String getFotoPrincipal() { return fotoPrincipal; }
    public void setFotoPrincipal(String fotoPrincipal) { this.fotoPrincipal = fotoPrincipal; }

    public boolean isEstacionamiento() { return estacionamiento; }
    public void setEstacionamiento(boolean estacionamiento) { this.estacionamiento = estacionamiento; }

    public boolean isLocomocionColectiva() { return locomocionColectiva; }
    public void setLocomocionColectiva(boolean locomocionColectiva) { this.locomocionColectiva = locomocionColectiva; }

    public boolean isServicioComida() { return servicioComida; }
    public void setServicioComida(boolean servicioComida) { this.servicioComida = servicioComida; }

    public boolean isAlojamiento() { return alojamiento; }
    public void setAlojamiento(boolean alojamiento) { this.alojamiento = alojamiento; }

    public boolean isDestacado() { return destacado; }
    public void setDestacado(boolean destacado) { this.destacado = destacado; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
}
