package duoc.eventogamer.controller;

import duoc.eventogamer.model.Perfil;
import duoc.eventogamer.model.PerfilForm;
import duoc.eventogamer.service.PerfilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PerfilControllerTest {

    @Test
    @DisplayName("verPerfil y guardarPerfil muestran y guardan el perfil autenticado")
    void verYGuardarPerfil() {
        PerfilService service = new PerfilService();
        PerfilController controller = new PerfilController(service);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("fernando");

        ConcurrentModel model = new ConcurrentModel();
        assertEquals("perfil", controller.verPerfil(auth, model));

        PerfilForm form = PerfilForm.desde(new Perfil("fernando"));
        form.setGamertag("Fer_Pro");
        form.setAvatar("🎮");
        form.setJuegos(List.of("Valorant"));

        BeanPropertyBindingResult result = new BeanPropertyBindingResult(form, "perfilForm");
        assertEquals("redirect:/perfil?guardado", controller.guardarPerfil(form, result, auth, new ConcurrentModel()));
    }
}
