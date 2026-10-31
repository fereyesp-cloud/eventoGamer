package duoc.eventogamer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Usuarios iniciales con contraseñas encriptadas con BCrypt.
     * Al separar esta configuración en ApplicationConfig, se evitan dependencias
     * circulares con el SecurityFilterChain y JwtAuthenticationFilter.
     * Cuando tu compañero agregue JPA + MySQL, este bean se conecta al repositorio.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("fernando")
                .password(encoder.encode("Gamer2026!"))
                .roles("USER")
                .build(),
            User.withUsername("gina")
                .password(encoder.encode("Gamer2026!"))
                .roles("USER")
                .build(),
            User.withUsername("admin")
                .password(encoder.encode("Admin2026!"))
                .roles("USER", "ADMIN")
                .build()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

