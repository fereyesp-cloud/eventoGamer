package duoc.eventogamer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String texto;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private int orden = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Banner() {}

    public Banner(String texto, int orden) {
        this.texto = texto;
        this.orden = orden;
        this.activo = true;
    }

    // getters y setters (generarlos con VS Code)
}