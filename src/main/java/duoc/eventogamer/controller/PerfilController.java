package duoc.eventogamer.controller;

import duoc.eventogamer.model.Perfil;
import duoc.eventogamer.model.PerfilForm;
import duoc.eventogamer.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        // El usuario se obtiene de la sesión, NUNCA de la URL ni del formulario
        Perfil perfil = perfilService.obtener(authentication.getName());
        model.addAttribute("perfil", perfil);
        model.addAttribute("perfilForm", PerfilForm.desde(perfil));
        agregarOpciones(model);
        return "perfil";
    }

    @PostMapping("/perfil")
    public String guardarPerfil(@Valid @ModelAttribute("perfilForm") PerfilForm form,
                                BindingResult result,
                                Authentication authentication,
                                Model model) {

        if (form.getAvatar() == null || !PerfilService.AVATARES.contains(form.getAvatar())) {
            result.rejectValue("avatar", "invalido", "Selecciona un avatar válido");
        }
        if (form.getJuegos() != null && !PerfilService.JUEGOS.containsAll(form.getJuegos())) {
            result.rejectValue("juegos", "invalido", "Hay juegos no válidos en la selección");
        }

        if (result.hasErrors()) {
            model.addAttribute("perfil", perfilService.obtener(authentication.getName()));
            agregarOpciones(model);
            return "perfil";
        }

        perfilService.actualizar(authentication.getName(), form);
        return "redirect:/perfil?guardado";
    }

    private void agregarOpciones(Model model) {
        model.addAttribute("avatares", PerfilService.AVATARES);
        model.addAttribute("juegos", PerfilService.JUEGOS);
    }
}