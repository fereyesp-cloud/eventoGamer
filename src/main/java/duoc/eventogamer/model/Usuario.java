package duoc.eventogamer.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)   // hash BCrypt (60 caracteres)
    private String password;

    @Column(nullable = false)
    private boolean activo = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol", length = 20)
    private Set<String> roles = new HashSet<>();

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Usuario(String username, String passwordCifrada, Set<String> roles) {
        this.username = username;
        this.password = passwordCifrada;
        this.roles = new HashSet<>(roles);
        this.activo = true;
    }

    // getters y setters (generarlos con VS Code)
}