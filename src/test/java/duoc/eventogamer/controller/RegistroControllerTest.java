package duoc.eventogamer.controller;

import duoc.eventogamer.model.RegistroForm;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.UsuarioRepository;
import duoc.eventogamer.service.PerfilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistroControllerTest {

    @Test
    @DisplayName("mostrarRegistro y registrar crean un usuario nuevo con rol USER")
    void mostrarYRegistrarUsuario() {
        UsuarioRepository repo = mock(UsuarioRepository.class);
        PerfilService perfilService = new PerfilService();
        RegistroController controller = new RegistroController(repo, new BCryptPasswordEncoder(), perfilService);

        assertEquals("registro", controller.mostrarRegistro(new ConcurrentModel()));

        RegistroForm form = new RegistroForm();
        form.setNombre("Camila");
        form.setCorreo("camila@duoc.cl");
        form.setUsuario("camila_gamer");
        form.setContrasena("ClaveSegura2026!");
        form.setRepetirContrasena("ClaveSegura2026!");

        when(repo.existsByUsername("camila_gamer")).thenReturn(false);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(form, "registroForm");

        assertEquals("redirect:/login?registrado", controller.registrar(form, bindingResult));
        verify(repo).save(any(Usuario.class));
    }
}
