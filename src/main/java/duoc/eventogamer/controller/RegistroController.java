package duoc.eventogamer.controller;

import duoc.eventogamer.model.RegistroForm;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import duoc.eventogamer.service.PerfilService;

@Controller
public class RegistroController {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final PerfilService perfilService;

    public RegistroController(InMemoryUserDetailsManager userDetailsManager,
                          PasswordEncoder passwordEncoder,
                          PerfilService perfilService) {
    this.userDetailsManager = userDetailsManager;
    this.passwordEncoder = passwordEncoder;
    this.perfilService = perfilService;
}

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult result) {

        if (form.getContrasena() != null
                && !form.getContrasena().equals(form.getRepetirContrasena())) {
            result.rejectValue("repetirContrasena", "noCoincide", "Las contraseñas no coinciden");
        }

        if (form.getUsuario() != null && userDetailsManager.userExists(form.getUsuario())) {
            result.rejectValue("usuario", "duplicado", "Ese nombre de usuario ya está en uso");
        }

        if (result.hasErrors()) {
            return "registro";
        }

        userDetailsManager.createUser(
            User.withUsername(form.getUsuario())
                .password(passwordEncoder.encode(form.getContrasena()))
                .roles("USER")
                .build()
        );

        perfilService.crear(form.getUsuario(), form.getNombre(), form.getCorreo());

        return "redirect:/login?registrado";
    }
}