
package com.uptimealert.utime_alert;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "¡Uptime Alert Java está funcionando!";
    }

    @GetMapping("/api/health")
    public String comprobarEstado() {
        return "OK";
    }
}
