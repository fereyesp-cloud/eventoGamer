package duoc.eventogamer.dto;

import duoc.eventogamer.model.CanalCompartido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompartirEventoRequest {

    @NotNull(message = "El canal de compartido es obligatorio")
    private CanalCompartido canal;

    @Size(max = 300, message = "El mensaje no puede superar los 300 caracteres")
    private String mensaje;

    public CompartirEventoRequest() {}

    public CompartirEventoRequest(CanalCompartido canal, String mensaje) {
        this.canal = canal;
        this.mensaje = mensaje;
    }

    public CanalCompartido getCanal() { return canal; }
    public void setCanal(CanalCompartido canal) { this.canal = canal; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
