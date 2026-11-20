package duoc.eventogamer.dto;

import java.util.List;

public class ResumenValoracionesDTO {

    private Long eventoId;
    private double promedio;
    private long totalValoraciones;
    private Integer miValoracion;
    private List<ValoracionDTO> valoraciones;

    public ResumenValoracionesDTO() {}

    public ResumenValoracionesDTO(Long eventoId, double promedio, long totalValoraciones,
                                  Integer miValoracion, List<ValoracionDTO> valoraciones) {
        this.eventoId = eventoId;
        this.promedio = promedio;
        this.totalValoraciones = totalValoraciones;
        this.miValoracion = miValoracion;
        this.valoraciones = valoraciones;
    }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public double getPromedio() { return promedio; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    public long getTotalValoraciones() { return totalValoraciones; }
    public void setTotalValoraciones(long totalValoraciones) { this.totalValoraciones = totalValoraciones; }

    public Integer getMiValoracion() { return miValoracion; }
    public void setMiValoracion(Integer miValoracion) { this.miValoracion = miValoracion; }

    public List<ValoracionDTO> getValoraciones() { return valoraciones; }
    public void setValoraciones(List<ValoracionDTO> valoraciones) { this.valoraciones = valoraciones; }
}
