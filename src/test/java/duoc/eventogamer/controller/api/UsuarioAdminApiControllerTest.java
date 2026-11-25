package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.ActualizarUsuarioRequest;
import duoc.eventogamer.dto.CrearUsuarioRequest;
import duoc.eventogamer.dto.UsuarioAdminDTO;
import duoc.eventogamer.service.UsuarioAdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsuarioAdminApiControllerTest {

    @Test
    @DisplayName("gestiona listado, creación, actualización, estado y eliminación de usuarios")
    void crudUsuariosAdmin() {
        UsuarioAdminService service = mock(UsuarioAdminService.class);
        UsuarioAdminApiController controller = new UsuarioAdminApiController(service);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");

        UsuarioAdminDTO dto = new UsuarioAdminDTO(2L, "nuevo_admin", true, List.of("ADMIN", "USER"));
        when(service.listarTodos()).thenReturn(List.of(dto));
        when(service.obtenerPorId(2L)).thenReturn(dto);
        when(service.crearUsuario(any())).thenReturn(dto);
        when(service.actualizarUsuario(eq(2L), any(), eq("admin"))).thenReturn(dto);
        when(service.cambiarEstado(2L, false, "admin")).thenReturn(dto);

        assertEquals(200, controller.listarUsuarios().getStatusCode().value());
        assertEquals(200, controller.obtenerUsuario(2L).getStatusCode().value());
        assertEquals(201, controller.crearUsuario(new CrearUsuarioRequest("nuevo_admin", "ClaveSegura2026!", Set.of("ADMIN"), true)).getStatusCode().value());
        assertEquals(200, controller.actualizarUsuario(2L, new ActualizarUsuarioRequest(Set.of("USER"), true, null), auth).getStatusCode().value());
        assertEquals(200, controller.cambiarEstado(2L, Map.of("activo", false), auth).getStatusCode().value());
        assertEquals(204, controller.eliminarUsuario(2L, auth).getStatusCode().value());
    }
}
