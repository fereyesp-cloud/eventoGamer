package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.ActualizarUsuarioRequest;
import duoc.eventogamer.dto.CrearUsuarioRequest;
import duoc.eventogamer.dto.UsuarioAdminDTO;
import duoc.eventogamer.service.UsuarioAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * [Privada - ROLE_ADMIN] API REST para gestionar usuarios administradores y usuarios del sitio.
 * Requiere token JWT válido perteneciente a un usuario con rol ADMIN.
 */
@RestController
@RequestMapping("/api/admin/usuarios")
public class UsuarioAdminApiController {

    private final UsuarioAdminService usuarioAdminService;

    public UsuarioAdminApiController(UsuarioAdminService usuarioAdminService) {
        this.usuarioAdminService = usuarioAdminService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioAdminDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioAdminService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAdminDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioAdminService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioAdminDTO> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioAdminDTO creado = usuarioAdminService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAdminDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request,
            Authentication authentication) {
        UsuarioAdminDTO actualizado = usuarioAdminService.actualizarUsuario(id, request, authentication.getName());
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioAdminDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body,
            Authentication authentication) {
        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(usuarioAdminService.cambiarEstado(id, activo, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id, Authentication authentication) {
        usuarioAdminService.eliminarUsuario(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
