package duoc.eventogamer.dto;

import java.util.List;

public class UsuarioAdminDTO {

    private Long id;
    private String username;
    private boolean activo;
    private List<String> roles;
    private boolean administrador;

    public UsuarioAdminDTO() {}

    public UsuarioAdminDTO(Long id, String username, boolean activo, List<String> roles) {
        this.id = id;
        this.username = username;
        this.activo = activo;
        this.roles = roles;
        this.administrador = roles != null && roles.contains("ADMIN");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) {
        this.roles = roles;
        this.administrador = roles != null && roles.contains("ADMIN");
    }

    public boolean isAdministrador() { return administrador; }
    public void setAdministrador(boolean administrador) { this.administrador = administrador; }
}
