package duoc.eventogamer.controller;

import duoc.eventogamer.dto.UsuarioAdminDTO;
import duoc.eventogamer.service.UsuarioAdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminUsuarioControllerTest {

    @Test
    @DisplayName("panelUsuarios carga lista y crearUsuario redirige a /admin/usuarios")
    void panelYCreacionDeUsuarios() {
        UsuarioAdminService service = mock(UsuarioAdminService.class);
        AdminUsuarioController controller = new AdminUsuarioController(service);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");
        when(service.listarTodos()).thenReturn(List.of(new UsuarioAdminDTO(1L, "admin", true, List.of("ADMIN", "USER"))));

        ConcurrentModel model = new ConcurrentModel();
        assertEquals("admin-usuarios", controller.panelUsuarios(auth, model));
        assertEquals("admin", model.getAttribute("adminActual"));

        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();
        assertEquals("redirect:/admin/usuarios", controller.crearUsuario("nuevo_user", "ClaveSegura2026!", true, redirect));
        verify(service).crearUsuario(any());
    }
}
