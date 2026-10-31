package duoc.eventogamer.controller;

import duoc.eventogamer.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventoService eventoService;

    public HomeController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        // Nuevos atributos según rúbrica: más recientes, populares y avisos comerciales
        model.addAttribute("recientes", eventoService.obtenerRecientes());
        model.addAttribute("populares", eventoService.obtenerPopulares());
        model.addAttribute("banners", eventoService.obtenerBanners());

        // Compatibilidad hacia atrás
        model.addAttribute("proximos", eventoService.proximos());
        model.addAttribute("destacados", eventoService.destacados());
        model.addAttribute("marcas", eventoService.obtenerBanners());
        return "index";
    }
}
