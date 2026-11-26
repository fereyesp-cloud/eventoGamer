package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistroFormTest {

    @Test
    @DisplayName("RegistroForm almacena y retorna todos los campos del formulario")
    void gettersYSetters() {
        RegistroForm form = new RegistroForm();
        form.setNombre("Matias");
        form.setCorreo("matias@duoc.cl");
        form.setUsuario("matias_dev");
        form.setContrasena("ClaveSegura2026!");
        form.setRepetirContrasena("ClaveSegura2026!");

        assertEquals("Matias", form.getNombre());
        assertEquals("matias@duoc.cl", form.getCorreo());
        assertEquals("matias_dev", form.getUsuario());
        assertEquals("ClaveSegura2026!", form.getContrasena());
        assertEquals("ClaveSegura2026!", form.getRepetirContrasena());
    }
}
