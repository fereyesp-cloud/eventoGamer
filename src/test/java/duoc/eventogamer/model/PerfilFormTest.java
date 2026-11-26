package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerfilFormTest {

    @Test
    @DisplayName("PerfilForm.desde copia correctamente las propiedades editables desde Perfil")
    void desdePerfil() {
        Perfil perfil = new Perfil("fernando");
        perfil.setGamertag("Fer_CL");
        perfil.setAvatar("🤖");
        perfil.setJuegos(List.of("Valorant", "Counter-Strike 2"));
        perfil.setNotifCorreo(true);

        PerfilForm form = PerfilForm.desde(perfil);

        assertEquals("Fer_CL", form.getGamertag());
        assertEquals("🤖", form.getAvatar());
        assertEquals(2, form.getJuegos().size());
        assertTrue(form.isNotifCorreo());
    }
}
