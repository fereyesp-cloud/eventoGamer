package duoc.eventogamer.controller;

import duoc.eventogamer.model.RegistroForm;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.UsuarioRepository;
import duoc.eventogamer.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class RegistroController {

    
    private final PasswordEncoder passwordEncoder;
    private final PerfilService perfilService;
    private final UsuarioRepository usuarioRepository;
    

    public RegistroController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          PerfilService perfilService) {
        this.usuarioRepository = usuarioRepository;
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

        if (form.getUsuario() != null && usuarioRepository.existsByUsername(form.getUsuario())) {
            result.rejectValue("usuario", "duplicado", "Ese nombre de usuario ya está en uso");
        }

        if (result.hasErrors()) {
            return "registro";
        }

        usuarioRepository.save(new Usuario(
        form.getUsuario(),
            passwordEncoder.encode(form.getContrasena()),
            Set.of("USER")
        ));

        perfilService.crear(form.getUsuario(), form.getNombre(), form.getCorreo());

        return "redirect:/login?registrado";
    }
}