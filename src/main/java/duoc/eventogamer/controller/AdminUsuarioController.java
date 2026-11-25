package duoc.eventogamer.controller;

import duoc.eventogamer.dto.CrearUsuarioRequest;
import duoc.eventogamer.service.UsuarioAdminService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

/**
 * [Privada - ROLE_ADMIN] Panel web Thymeleaf para gestionar usuarios del sitio y administradores.
 */
@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private static final String REDIRECT_ADMIN = "redirect:/admin/usuarios";
    private static final String ATTR_EXITO = "mensajeExito";
    private static final String ATTR_ERROR = "mensajeError";

    private final UsuarioAdminService usuarioAdminService;

    public AdminUsuarioController(UsuarioAdminService usuarioAdminService) {
        this.usuarioAdminService = usuarioAdminService;
    }

    @GetMapping
    public String panelUsuarios(Authentication authentication, Model model) {
        model.addAttribute("usuarios", usuarioAdminService.listarTodos());
        model.addAttribute("adminActual", authentication.getName());
        return "admin-usuarios";
    }

    @PostMapping
    public String crearUsuario(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam(value = "esAdmin", defaultValue = "false") boolean esAdmin,
                               RedirectAttributes redirectAttributes) {
        Set<String> roles = new HashSet<>();
        roles.add(UsuarioAdminService.ROL_USER);
        if (esAdmin) {
            roles.add(UsuarioAdminService.ROL_ADMIN);
        }
        try {
            usuarioAdminService.crearUsuario(new CrearUsuarioRequest(username, password, roles, true));
            redirectAttributes.addFlashAttribute(ATTR_EXITO,
                    "Usuario '" + username.trim() + "' creado correctamente.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_ADMIN;
    }

    @PostMapping("/{id}/rol-admin")
    public String cambiarRolAdmin(@PathVariable Long id,
                                  @RequestParam("otorgar") boolean otorgar,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioAdminService.cambiarRolAdmin(id, otorgar, authentication.getName());
            redirectAttributes.addFlashAttribute(ATTR_EXITO,
                    otorgar ? "Rol de administrador asignado." : "Rol de administrador removido.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_ADMIN;
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam("activo") boolean activo,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            usuarioAdminService.cambiarEstado(id, activo, authentication.getName());
            redirectAttributes.addFlashAttribute(ATTR_EXITO,
                    activo ? "Cuenta activada correctamente." : "Cuenta desactivada correctamente.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_ADMIN;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioAdminService.eliminarUsuario(id, authentication.getName());
            redirectAttributes.addFlashAttribute(ATTR_EXITO, "Usuario eliminado correctamente.");
        } catch (ResponseStatusException ex) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, ex.getReason());
        }
        return REDIRECT_ADMIN;
    }
}
