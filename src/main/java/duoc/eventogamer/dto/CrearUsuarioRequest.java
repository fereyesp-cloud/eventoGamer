package duoc.eventogamer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CrearUsuarioRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$",
             message = "El usuario debe tener entre 3 y 20 caracteres (letras, números o guion bajo)")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    private String password;

    private Set<String> roles;

    private Boolean activo = true;

    public CrearUsuarioRequest() {}

    public CrearUsuarioRequest(String username, String password, Set<String> roles, Boolean activo) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.activo = activo;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
