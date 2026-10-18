
package com.uptimealert.utime_alert;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PatchMapping;






@RestController
@RequestMapping("/api/config")
public class ConfiguracionControlador {

   private final ConfiguracionRepositorio configuracionRepositorio;
private final ServicioTelegram servicioTelegram;

public ConfiguracionControlador(
        ConfiguracionRepositorio configuracionRepositorio,
        ServicioTelegram servicioTelegram) {

    this.configuracionRepositorio = configuracionRepositorio;
    this.servicioTelegram = servicioTelegram;
}
    





    // Consulta la configuración guardada.
    @GetMapping
    public Map<String, String> consultar() {
        Configuracion configuracion = configuracionRepositorio.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (configuracion == null) {
            return Map.of(
                    "telegram_token", "",
                    "telegram_chat_id", ""
            );
        }

        return Map.of(
                "telegram_token", "",
                "telegram_chat_id",
                configuracion.getTelegramChatId() == null
                        ? ""
                        : configuracion.getTelegramChatId()
        );
    }

    // Guarda o actualiza la configuración de Telegram.
    @PostMapping
    public Map<String, String> guardar(
            @RequestBody Map<String, String> datos) {

        String token = datos.get("telegram_token");
        String chatId = datos.get("telegram_chat_id");

        if (token == null || token.isBlank()
                || chatId == null || chatId.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El token y el identificador del chat son obligatorios"
            );
        }

        Configuracion configuracion = configuracionRepositorio.findAll()
                .stream()
                .findFirst()
                .orElseGet(Configuracion::new);

        configuracion.setTelegramToken(token.trim());
        configuracion.setTelegramChatId(chatId.trim());

        configuracionRepositorio.save(configuracion);

        return Map.of("mensaje", "Configuración guardada correctamente");
    }


    @PatchMapping("/estado")
public Map<String, String> cambiarEstado(
        @RequestBody Map<String, Boolean> datos) {

    Boolean activo = datos.get("activo");

    if (activo == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Debes indicar si Telegram está activo"
        );
    }

    Configuracion configuracion = configuracionRepositorio.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todavía no hay una configuración de Telegram guardada"
            ));

    configuracion.setActivo(activo);
    configuracionRepositorio.save(configuracion);

    return Map.of(
        "mensaje",
        activo ? "Telegram activado" : "Telegram desactivado"
    );
}

@PostMapping("/probar")
public Map<String, String> probarTelegram() {
    Configuracion configuracion = configuracionRepositorio.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Primero debes guardar la configuración de Telegram"));

    if (!configuracion.isActivo()) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Telegram está desactivado. Actívalo antes de hacer la prueba.");
    }

    String token = configuracion.getTelegramToken();
    String chatId = configuracion.getTelegramChatId();

    try {
        servicioTelegram.enviarMensaje(
                token,
                chatId,
                "✅ Prueba de Uptime Alert Java: Telegram funciona correctamente.");

        return Map.of("mensaje", "Mensaje de prueba enviado correctamente");

    } catch (Exception e) {
    System.err.println(
            "Error Telegram: " + e.getClass().getSimpleName()
            + " - " + e.getMessage());

    throw new ResponseStatusException(
            HttpStatus.BAD_GATEWAY,
            "No se pudo enviar el mensaje de prueba. Consulta la terminal del servidor."
    );
}
}



}



/*
- GET /api/config: consulta la configuración sin revelar el token secreto.
- POST /api/config: guarda la configuración o actualiza el registro existente.
- Comprueba que ambos campos estén rellenados.
- Utiliza ConfiguracionRepositorio para persistir los datos en MySQL.
Nota de seguridad: el token se almacena en la base de datos, pero no se devuelve al consultar la configuración. Más adelante podremos adaptar el formulario para indicar que ya hay un token guardado sin mostrarlo.

*/
