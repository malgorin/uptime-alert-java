
package com.uptimealert.utime_alert;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class ServicioTelegram {

    private final HttpClient clienteHttp = HttpClient.newHttpClient();

    public void enviarMensaje(String token, String chatId, String mensaje)
            throws Exception {

        if (token == null || token.isBlank()
                || chatId == null || chatId.isBlank()) {
            throw new IllegalArgumentException(
                    "El token y el identificador del chat son obligatorios.");
        }

        String datos = "chat_id="
                + URLEncoder.encode(chatId, StandardCharsets.UTF_8)
                + "&text="
                + URLEncoder.encode(mensaje, StandardCharsets.UTF_8);

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.telegram.org/bot" + token + "/sendMessage"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(datos))
                .build();

        HttpResponse<String> respuesta = clienteHttp.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString());

       if (respuesta.statusCode() != 200
        || !respuesta.body().contains("\"ok\":true")) {
    throw new IllegalStateException(
            "Telegram respondió HTTP " + respuesta.statusCode()
                    + ": " + respuesta.body());
}
    }
}



/*
@Service permite que Spring Boot utilice esta clase desde otros componentes.
enviarMensaje(...) recibe el token, el Chat ID y el texto.
La petición se envía a la API oficial de Telegram.
Si Telegram devuelve un error, el método lo comunica en vez de indicar que el envío ha funcionado.
*/
