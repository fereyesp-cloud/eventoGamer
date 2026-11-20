package duoc.eventogamer.dto;

import duoc.eventogamer.model.TipoMedia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventoMediaRequest {

    @NotNull(message = "El tipo de archivo multimedia es obligatorio (FOTO o VIDEO)")
    private TipoMedia tipo;

    @NotBlank(message = "La URL del archivo multimedia es obligatoria")
    @Size(max = 500, message = "La URL no puede superar los 500 caracteres")
    private String url;

    @Size(max = 120, message = "El título no puede superar los 120 caracteres")
    private String titulo;

    public EventoMediaRequest() {}

    public EventoMediaRequest(TipoMedia tipo, String url, String titulo) {
        this.tipo = tipo;
        this.url = url;
        this.titulo = titulo;
    }

    public TipoMedia getTipo() { return tipo; }
    public void setTipo(TipoMedia tipo) { this.tipo = tipo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
}
