package duoc.eventogamer.model;

import duoc.eventogamer.dto.EventoDetalleDTO;
import duoc.eventogamer.dto.EventoResumenDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {

    private Long id;
    private String nombre;
    private String descripcion;
    private String videojuego;
    private String categoria;
    private String pais;
    private String ciudad;
    private String modalidad; // "Presencial" u "Online"
    private LocalDate fecha;
    private LocalTime hora;
    private String ubicacion;
    private String direccion;
    private String enlaceAcceso;
    private String organizador;
    private List<String> organizadores = new ArrayList<>();
    private List<String> expositores = new ArrayList<>();
    private String requisitosParticipacion;
    private String informacionInscripcion;
    private List<String> fotografias = new ArrayList<>();
    private String fotoPrincipal;
    private boolean estacionamiento;
    private boolean locomocionColectiva;
    private boolean servicioComida;
    private boolean alojamiento;
    private boolean destacado;
    private boolean popular;
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
