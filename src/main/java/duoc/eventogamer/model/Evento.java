package duoc.eventogamer.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Evento {

    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private LocalDate fecha;
    private LocalTime hora;
    private String ubicacion;   
    private String direccion;
    private List<String> organizadores;
    private List<String> expositores;
    private boolean estacionamiento;
    private boolean locomocionColectiva;
    private boolean servicioComida;
    private boolean alojamiento;
    private boolean destacado;

    public Evento(Long id, String nombre, String descripcion, String categoria,
                  LocalDate fecha, LocalTime hora, String ubicacion, String direccion,
                  List<String> organizadores, List<String> expositores,
                  boolean estacionamiento, boolean locomocionColectiva,
                  boolean servicioComida, boolean alojamiento, boolean destacado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
        this.direccion = direccion;
        this.organizadores = organizadores;
        this.expositores = expositores;
        this.estacionamiento = estacionamiento;
        this.locomocionColectiva = locomocionColectiva;
        this.servicioComida = servicioComida;
        this.alojamiento = alojamiento;
        this.destacado = destacado;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getUbicacion() { return ubicacion; }
    public String getDireccion() { return direccion; }
    public List<String> getOrganizadores() { return organizadores; }
    public List<String> getExpositores() { return expositores; }
    public boolean isEstacionamiento() { return estacionamiento; }
    public boolean isLocomocionColectiva() { return locomocionColectiva; }
    public boolean isServicioComida() { return servicioComida; }
    public boolean isAlojamiento() { return alojamiento; }
    public boolean isDestacado() { return destacado; }
}