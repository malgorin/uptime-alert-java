/*
Usaremos Spring Data JPA, que nos permite trabajar con objetos Java en lugar de escribir todas las consultas SQL manualmente.

entidad Equipo.java

@Entity indica que Java representa un registro de la base de datos.
@Table(name = "equipos") establece el nombre de la tabla.
@Id identifica la clave primaria.
@GeneratedValue permite que MySQL genere el ID automáticamente.
El constructor vacío es necesario para JPA.

*/
package com.uptimealert.utime_alert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ip;
    private String estado = "DESCONOCIDO";

    public Equipo() {
    }

    public Equipo(String nombre, String ip) {
        this.nombre = nombre;
        this.ip = ip;
        this.estado = "DESCONOCIDO";
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
