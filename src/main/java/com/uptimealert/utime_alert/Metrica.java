
package com.uptimealert.utime_alert;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "metricas")
public class Metrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostname;
    private Double cpu;
    private Double ram;
    private Double disco;
    private LocalDateTime fecha;

    public Metrica() {
    }

    public Long getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getRam() {
        return ram;
    }

    public void setRam(Double ram) {
        this.ram = ram;
    }

    public Double getDisco() {
        return disco;
    }

    public void setDisco(Double disco) {
        this.disco = disco;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}


/*
Campo	Significado
hostname	Nombre del equipo
cpu	Porcentaje de uso de CPU
ram	Porcentaje de uso de RAM
disco	Porcentaje de uso del disco
fecha	Momento en que se recibió la medición
JPA creará la tabla metricas al iniciar la aplicación, porque tienes configurado spring.jpa.hibernate.ddl-auto=update.
*/