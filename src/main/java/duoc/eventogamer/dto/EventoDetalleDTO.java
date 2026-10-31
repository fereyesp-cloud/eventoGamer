package duoc.eventogamer.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventoDetalleDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String videojuego;
    private String categoria;
    private String pais;
    private String ciudad;
    private String modalidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String ubicacion;
    private String enlaceAcceso;
    private String organizador;
    private String requisitosParticipacion;
    private String informacionInscripcion;
    private List<String> fotografias;
    private boolean popular;

    public EventoDetalleDTO() {}

    public EventoDetalleDTO(Long id, String nombre, String descripcion, String videojuego,
                            String categoria, String pais, String ciudad, String modalidad,
                            LocalDate fecha, LocalTime hora, String ubicacion, String enlaceAcceso,
                            String organizador, String requisitosParticipacion,
                            String informacionInscripcion, List<String> fotografias, boolean popular) {
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
        this.enlaceAcceso = enlaceAcceso;
        this.organizador = organizador;
        this.requisitosParticipacion = requisitosParticipacion;
        this.informacionInscripcion = informacionInscripcion;
        this.fotografias = fotografias;
        this.popular = popular;
    }

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

    public String getEnlaceAcceso() { return enlaceAcceso; }
    public void setEnlaceAcceso(String enlaceAcceso) { this.enlaceAcceso = enlaceAcceso; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public String getRequisitosParticipacion() { return requisitosParticipacion; }
    public void setRequisitosParticipacion(String requisitosParticipacion) { this.requisitosParticipacion = requisitosParticipacion; }

    public String getInformacionInscripcion() { return informacionInscripcion; }
    public void setInformacionInscripcion(String informacionInscripcion) { this.informacionInscripcion = informacionInscripcion; }

    public List<String> getFotografias() { return fotografias; }
    public void setFotografias(List<String> fotografias) { this.fotografias = fotografias; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}

