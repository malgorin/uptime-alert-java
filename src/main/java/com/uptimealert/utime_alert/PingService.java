/*  comprobador de ping

En Windows ejecuta ping -n 1.
En Linux ejecuta ping -c 1.
Espera una respuesta durante un tiempo limitado.
Devuelve true si el equipo responde y false si no responde o se produce un error.

*/

package com.uptimealert.utime_alert;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class PingService {

    public boolean comprobar(String ip) {
        boolean windows = System.getProperty("os.name")
                .toLowerCase(Locale.ROOT)
                .contains("win");

        ProcessBuilder builder;

        if (windows) {
            builder = new ProcessBuilder(
                    "ping", "-n", "1", "-w", "2000", ip);
        } else {
            builder = new ProcessBuilder(
                    "ping", "-c", "1", "-W", "2", ip);
        }

        builder.redirectErrorStream(true);

        try {
            Process proceso = builder.start();

            boolean termino = proceso.waitFor(4, TimeUnit.SECONDS);

            if (!termino) {
                proceso.destroyForcibly();
                return false;
            }

            return proceso.exitValue() == 0;

        } catch (IOException e) {
            System.err.println(
                    "No se pudo ejecutar ping. Comprueba que esté instalado: "
                    + e.getMessage());
            return false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
