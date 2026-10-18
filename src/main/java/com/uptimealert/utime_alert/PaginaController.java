
/*controlador de páginas

@Controller indica que vamos a mostrar páginas HTML, no devolver texto directamente.
@GetMapping("/login") conecta la dirección /login con templates/login.html.
@GetMapping("/panel") conecta /panel con templates/index.html.

return "login" y return "index" indican a Thymeleaf qué plantilla debe cargar.
*/




package com.uptimealert.utime_alert;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    private final UsuarioRepository usuarioRepository;

    public PaginaController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute(
                "hasUsers",
                usuarioRepository.count() > 0
        );
        return "login";
    }

    @GetMapping("/panel")
    public String mostrarPanel() {
        return "index";
    }
}
