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

@Controller
public class RegistroController {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(InMemoryUserDetailsManager userDetailsManager,
                              PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
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

        return "redirect:/login?registrado";
    }
}