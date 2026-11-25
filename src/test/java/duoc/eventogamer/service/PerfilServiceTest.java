package duoc.eventogamer.service;

import duoc.eventogamer.model.Perfil;
import duoc.eventogamer.model.PerfilForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerfilServiceTest {

    @Test
    @DisplayName("crear, obtener y actualizar gestionan correctamente los datos del perfil")
    void crearObtenerYActualizarPerfil() {
        PerfilService service = new PerfilService();

        service.crear("fernando", "Fernando Reyes", "fernando@duoc.cl");
        Perfil perfil = service.obtener("fernando");
        assertEquals("Fernando Reyes", perfil.getNombre());

        PerfilForm form = PerfilForm.desde(perfil);
        form.setGamertag("FerGamer99");
        form.setAvatar("🐉");
        form.setJuegos(List.of("Valorant"));
        form.setNotifCorreo(true);

        service.actualizar("fernando", form);
        Perfil actualizado = service.obtener("fernando");
        assertEquals("FerGamer99", actualizado.getGamertag());
        assertEquals("🐉", actualizado.getAvatar());
        assertTrue(actualizado.isNotifCorreo());
    }
}
