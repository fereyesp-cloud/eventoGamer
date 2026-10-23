package duoc.eventogamer.controller;

import duoc.eventogamer.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final EventoService eventoService;

    public HomeController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("proximos", eventoService.proximos());
        model.addAttribute("destacados", eventoService.destacados());
        model.addAttribute("marcas", List.of(
            "HyperGear - Periféricos gamer",
            "NovaChair - Sillas ergonómicas",
            "PixelNet - Internet fibra para gamers"
        ));
        return "index";
    }
}