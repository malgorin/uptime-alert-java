/*   servicio de autenticación

Cuando alguien envía el formulario de inicio de sesión:
1. Spring Security recibe username y password.
2. Este servicio busca el usuario en MySQL.
3. Spring Security compara la contraseña introducida con el hash BCrypt almacenado.
4. Si las credenciales son correctas, permite iniciar sesión; si no, rechaza el acceso.
No necesitamos escribir nosotros la comparación de contraseñas: Spring Security se encarga de ella utilizando el PasswordEncoder que configuramos anteriormente.

*/




package com.uptimealert.utime_alert;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuarios implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServicioUsuarios(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado"
                ));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .build();
    }
}
