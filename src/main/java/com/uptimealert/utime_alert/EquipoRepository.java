/*Al extender JpaRepository, Spring nos proporciona operaciones para guardar, consultar, actualizar y eliminar equipos, sin que tengamos que implementarlas manualmente.

*/


package com.uptimealert.utime_alert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
}
