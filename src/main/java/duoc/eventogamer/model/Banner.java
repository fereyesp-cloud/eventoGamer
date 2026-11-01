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

    public Banner() {}

    public Banner(String texto, int orden) {
        this.texto = texto;
        this.orden = orden;
        this.activo = true;
    }

    // getters y setters (generarlos con VS Code)
}