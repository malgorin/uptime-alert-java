/*   configuración de seguridad para no guardar contraseñas en texto plano   


- El formulario utiliza los campos username y password que ya tiene tu HTML.
- Si el inicio de sesión tiene éxito, redirige a /panel.
- Si falla, vuelve a /login?error=true.
- Las demás rutas quedan protegidas por autenticación, excepto /api/health y /api/metrics.
- BCrypt estará disponible para almacenar y verificar contraseñas de forma segura.


He dejado la protección CSRF de Spring Security activada. Cuando adaptemos las plantillas, tendremos que asegurarnos de que los formularios y las peticiones JavaScript de escritura envíen el token CSRF.


Esta configuración conecta explícitamente el servicio ServicioUsuarios con Spring Security y mantiene activada la protección CSRF

*/




package com.uptimealert.utime_alert;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain configurarSeguridad(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests(autorizacion -> autorizacion
                .requestMatchers(
                    "/login",
                    "/api/health",
                    "/api/metrics"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(formulario -> formulario
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/panel", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(cierre -> cierre
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            ServicioUsuarios servicioUsuarios,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider proveedor =
                new DaoAuthenticationProvider(servicioUsuarios);

        proveedor.setPasswordEncoder(passwordEncoder);

        return proveedor;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
