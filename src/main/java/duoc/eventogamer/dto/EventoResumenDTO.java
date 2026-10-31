package duoc.eventogamer.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventoResumenDTO {

    private Long id;
    private String nombre;
    private String videojuego;
    private String categoria;
    private String pais;
    private String ciudad;
    private String modalidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String fotoPrincipal;
    private boolean popular;

    public EventoResumenDTO() {}

    public EventoResumenDTO(Long id, String nombre, String videojuego, String categoria,
                            String pais, String ciudad, String modalidad,
                            LocalDate fecha, LocalTime hora, String fotoPrincipal, boolean popular) {
        this.id = id;
        this.nombre = nombre;
        this.videojuego = videojuego;
        this.categoria = categoria;
        this.pais = pais;
        this.ciudad = ciudad;
        this.modalidad = modalidad;
        this.fecha = fecha;
        this.hora = hora;
        this.fotoPrincipal = fotoPrincipal;
        this.popular = popular;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

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

    public String getFotoPrincipal() { return fotoPrincipal; }
    public void setFotoPrincipal(String fotoPrincipal) { this.fotoPrincipal = fotoPrincipal; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}

