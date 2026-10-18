/* Guardar y consulta Equipos Registrados 
dos operaciones:
- GET /api/equipos: consultar los equipos guardados.
- POST /api/equipos: registrar un equipo nuevo.
*/


package com.uptimealert.utime_alert;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoRepository equipoRepository;

    private final PingService pingService;

/*    
public EquipoController(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
*/


public EquipoController(EquipoRepository equipoRepository,PingService pingService) {
    this.equipoRepository = equipoRepository;
    this.pingService = pingService;
}

    @GetMapping
    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }


@GetMapping("/ping")
public java.util.Map<String, Object> comprobarIp(
        @RequestParam String ip) {

    boolean responde = pingService.comprobar(ip);

    return java.util.Map.of(
            "ip", ip,
            "responde", responde,
            "estado", responde ? "ACTIVO" : "INACTIVO"
    );
}

//Este método busca el equipo por su ID, comprueba su IP y guarda el nuevo estado en MySQL.
@GetMapping("/{id}/comprobar")
public Equipo comprobarEquipo(
        @org.springframework.web.bind.annotation.PathVariable Long id) {

    Equipo equipo = equipoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Equipo no encontrado"
            ));

    boolean responde = pingService.comprobar(equipo.getIp());

    equipo.setEstado(responde ? "ACTIVO" : "INACTIVO");

    return equipoRepository.save(equipo);
}


// Crea un equipo nuevo en MySQL. Valida que el nombre y la IP no estén vacíos.
    @PostMapping
    public Equipo crear(@RequestBody Equipo equipo) {
        if (equipo.getNombre() == null || equipo.getNombre().isBlank()
                || equipo.getIp() == null || equipo.getIp().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre y la IP son obligatorios"
            );
        }

        equipo.setEstado("DESCONOCIDO");
        return equipoRepository.save(equipo);
    }
/*
@RestController publica las operaciones como una API REST.
@RequestMapping("/api/equipos") establece la dirección base.
@GetMapping recupera los equipos de MySQL.
@PostMapping recibe los datos enviados en JSON y los guarda.
equipoRepository.save(equipo) realiza el guardado en la base de datos.
*/



// Elimina un equipo de MySQL usando su ID.
@DeleteMapping("/{id}")
public void eliminar(@PathVariable Long id) {

    Equipo equipo = equipoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Equipo no encontrado"
            ));

    equipoRepository.delete(equipo);
}
/*
@DeleteMapping("/{id}"): crea la ruta para eliminar un equipo concreto, por ejemplo, /api/equipos/3.
@PathVariable Long id: recoge el identificador que llega en la URL.
findById(id): comprueba que el equipo existe.
delete(equipo): elimina el registro de MySQL.
Si el equipo no existe, devuelve un error 404 Not Found
*/

}




