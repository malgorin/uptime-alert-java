
package com.uptimealert.utime_alert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// Esta entidad representa la configuración guardada en MySQL.
@Entity
@Table(name = "configuracion")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_token", length = 500)
    private String telegramToken;

    @Column(name = "telegram_chat_id", length = 100)
    private String telegramChatId;

    @Column(nullable = false)
    private boolean activo = true;

    public Configuracion() {
    }

    public Long getId() {
        return id;
    }

    public String getTelegramToken() {
        return telegramToken;
    }

    public void setTelegramToken(String telegramToken) {
        this.telegramToken = telegramToken;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public boolean isActivo() {
    return activo;
    }

    public void setActivo(boolean activo) {
    this.activo = activo;
    }
}


/*
- @Entity indica a JPA que esta clase se guarda en la base de datos.
- @Table(name = "configuracion") establece el nombre de la tabla.
- telegramToken y telegramChatId almacenan los valores de Telegram.
- Los métodos get y set permiten leer y modificar esos valores.
Spring Boot podrá crear la tabla automáticamente con la configuración JPA que ya tienes.
*/
