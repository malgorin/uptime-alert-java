
/*     Crear el administrador


Este componente crea la cuenta al arrancar la aplicación, solo si ese nombre de usuario todavía no existe. La contraseña se almacena como hash BCrypt, no en texto plano.

el usuario y la contraseña de inicio estan en el fichero .env

*/



package com.uptimealert.utime_alert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InicializarAdministrador implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USERNAME}")
    private String nombreAdministrador;

    @Value("${ADMIN_PASSWORD}")
    private String claveAdministrador;

    public InicializarAdministrador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (usuarioRepository.existsByUsername(nombreAdministrador)) {
            System.out.println("La cuenta administradora ya existe.");
            return;
        }

        Usuario administrador = new Usuario();
        administrador.setUsername(nombreAdministrador);
        administrador.setPassword(
                passwordEncoder.encode(claveAdministrador)
        );
        administrador.setRol("ADMIN");

        usuarioRepository.save(administrador);

        System.out.println("Cuenta administradora creada en MySQL.");
    }
}
