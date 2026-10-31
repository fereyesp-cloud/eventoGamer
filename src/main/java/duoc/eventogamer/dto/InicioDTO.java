package duoc.eventogamer.dto;

import java.util.List;

public class InicioDTO {

    private List<EventoResumenDTO> eventosRecientes;
    private List<EventoResumenDTO> eventosPopulares;
    private List<String> bannersAvisosComerciales;
    private String loginUrl;

    public InicioDTO() {}

    public InicioDTO(List<EventoResumenDTO> eventosRecientes,
                     List<EventoResumenDTO> eventosPopulares,
                     List<String> bannersAvisosComerciales,
                     String loginUrl) {
        this.eventosRecientes = eventosRecientes;
        this.eventosPopulares = eventosPopulares;
        this.bannersAvisosComerciales = bannersAvisosComerciales;
        this.loginUrl = loginUrl;
    }

    public List<EventoResumenDTO> getEventosRecientes() { return eventosRecientes; }
    public void setEventosRecientes(List<EventoResumenDTO> eventosRecientes) { this.eventosRecientes = eventosRecientes; }

    public List<EventoResumenDTO> getEventosPopulares() { return eventosPopulares; }
    public void setEventosPopulares(List<EventoResumenDTO> eventosPopulares) { this.eventosPopulares = eventosPopulares; }

    public List<String> getBannersAvisosComerciales() { return bannersAvisosComerciales; }
    public void setBannersAvisosComerciales(List<String> bannersAvisosComerciales) { this.bannersAvisosComerciales = bannersAvisosComerciales; }

    public String getLoginUrl() { return loginUrl; }
    public void setLoginUrl(String loginUrl) { this.loginUrl = loginUrl; }
}

