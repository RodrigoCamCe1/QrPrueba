package com.example.qrdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration  // Indica que esta clase define configuraciones de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring 
public class SecurityConfig {

    // Define el metodo de codificación de contraseñas (sin encriptar, es algo que solo nosotros vamos a usar)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Configura un usuario en memoria con credenciales fijas
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("admin")  // nombre de usuario
            .password("12345")                         // contraseña
            .roles("USER")                             // rol asignado
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Configura las reglas de seguridad HTTP (autorizaciones, login, CSRF, etc.)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva la protección CSRF (útil para pruebas)
            .csrf(csrf -> csrf.disable())

            // Define las rutas públicas y las que requieren autenticación
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )

           .formLogin(form -> form
                .loginPage("/login")                   // usa la plantilla login.html
                .defaultSuccessUrl("/index", true)     // después de login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true") // redirige tras logout
                .permitAll()
            );
        
        return http.build(); // Devuelve la cadena de filtros configurada
    }
}
