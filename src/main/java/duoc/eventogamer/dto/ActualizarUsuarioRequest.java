package duoc.eventogamer.dto;

import jakarta.validation.constraints.Size;

import java.util.Set;

public class ActualizarUsuarioRequest {

    private Set<String> roles;

    private Boolean activo;

    @Size(min = 8, max = 64, message = "La nueva contraseña debe tener entre 8 y 64 caracteres")
    private String nuevaPassword;

    public ActualizarUsuarioRequest() {}

    public ActualizarUsuarioRequest(Set<String> roles, Boolean activo, String nuevaPassword) {
        this.roles = roles;
        this.activo = activo;
        this.nuevaPassword = nuevaPassword;
    }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getNuevaPassword() { return nuevaPassword; }
    public void setNuevaPassword(String nuevaPassword) { this.nuevaPassword = nuevaPassword; }
}
