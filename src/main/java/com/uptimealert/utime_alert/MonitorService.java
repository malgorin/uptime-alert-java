/* servicio de monitorizacion automatica

clase que recorra los equipos guardados en MySQL, haga ping a cada uno y actualice su estado
*/




package com.uptimealert.utime_alert;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MonitorService {

    private final EquipoRepository equipoRepository;
    private final PingService pingService;
    private final ConfiguracionRepositorio configuracionRepositorio;
    private final ServicioTelegram servicioTelegram;

    public MonitorService(
            EquipoRepository equipoRepository,
            PingService pingService,
            ConfiguracionRepositorio configuracionRepositorio,
            ServicioTelegram servicioTelegram) {

        this.equipoRepository = equipoRepository;
        this.pingService = pingService;
        this.configuracionRepositorio = configuracionRepositorio;
        this.servicioTelegram = servicioTelegram;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 2000)
    public void comprobarEquipos() {

        List<Equipo> equipos = equipoRepository.findAll();

        for (Equipo equipo : equipos) {

            String estadoAnterior = equipo.getEstado();
            boolean responde = pingService.comprobar(equipo.getIp());

            String nuevoEstado = responde ? "ACTIVO" : "INACTIVO";

            equipo.setEstado(nuevoEstado);
            equipoRepository.save(equipo);

            System.out.println(
                    "Equipo: " + equipo.getNombre()
                    + " | IP: " + equipo.getIp()
                    + " | Estado: " + nuevoEstado
            );

            // Solo avisar si el equipo cambia de estado.
            if (estadoAnterior != null
                    && !estadoAnterior.equals(nuevoEstado)) {

                enviarAlerta(equipo, nuevoEstado);
            }
        }
    }

    private void enviarAlerta(Equipo equipo, String estado) {

        var configuracion =
                configuracionRepositorio.findAll()
                        .stream()
                        .findFirst();

        if (configuracion.isEmpty()
                || !configuracion.get().isActivo()) {
            return;
        }

        String mensaje;

        if ("INACTIVO".equals(estado)) {
            mensaje = "🔴 ALERTA: El equipo "
                    + equipo.getNombre()
                    + " (" + equipo.getIp()
                    + ") ha dejado de responder.";
        } else {
            mensaje = "🟢 RECUPERACIÓN: El equipo "
                    + equipo.getNombre()
                    + " (" + equipo.getIp()
                    + ") vuelve a estar disponible.";
        }

        try {
            servicioTelegram.enviarMensaje(
                    configuracion.get().getTelegramToken(),
                    configuracion.get().getTelegramChatId(),
                    mensaje
            );

        } catch (Exception e) {
            System.err.println(
                    "No se pudo enviar la alerta de Telegram: "
                    + e.getMessage()
            );
        }
    }
}

