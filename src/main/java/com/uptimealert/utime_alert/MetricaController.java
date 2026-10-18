
package com.uptimealert.utime_alert;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class MetricaController {

    private final MetricaRepository metricaRepository;

    public MetricaController(MetricaRepository metricaRepository) {
        this.metricaRepository = metricaRepository;
    }

    @PostMapping
    public ResponseEntity<String> recibirMetrica(
            @RequestBody Metrica metrica) {

        if (metrica.getHostname() == null
                || metrica.getHostname().isBlank()
                || metrica.getCpu() == null
                || metrica.getRam() == null
                || metrica.getDisco() == null) {

            return ResponseEntity.badRequest()
                    .body("Faltan datos de la métrica.");
        }

        if (!Double.isFinite(metrica.getCpu())
                || !Double.isFinite(metrica.getRam())
                || !Double.isFinite(metrica.getDisco())
                || metrica.getCpu() < 0 || metrica.getCpu() > 100
                || metrica.getRam() < 0 || metrica.getRam() > 100
                || metrica.getDisco() < 0 || metrica.getDisco() > 100) {

            return ResponseEntity.badRequest()
                    .body("Los porcentajes deben estar entre 0 y 100.");
        }

        metrica.setFecha(LocalDateTime.now());
        metricaRepository.save(metrica);

        return ResponseEntity.ok("Métrica recibida correctamente.");
    }
}

/*
Recibir peticiones POST en /api/metrics.
Validar los valores de CPU, RAM y disco.
Guardar cada medición con su fecha en MySQL.
*/

