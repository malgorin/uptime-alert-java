
package com.uptimealert.utime_alert;

import org.springframework.data.jpa.repository.JpaRepository;

// Permite consultar y guardar configuraciones en MySQL.

public interface ConfiguracionRepositorio
        extends JpaRepository<Configuracion, Long> {
}
