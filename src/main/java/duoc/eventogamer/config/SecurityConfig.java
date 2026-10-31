package duoc.eventogamer.config;

import duoc.eventogamer.jwt.JwtAuthenticationEntryPoint;
import duoc.eventogamer.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para las APIs REST (el token JWT protege contra CSRF por ser stateless)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            // Manejo de excepciones de autenticación (401 JSON para APIs)
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))

            // Política de sesiones: híbrida para soportar tanto navegación web como APIs REST sin estado
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authorizeHttpRequests(auth -> auth
                // ==========================================
                // 1. APIs Públicas especificadas en la rúbrica:
                // ==========================================
                // [Pública] Inicio: eventos recientes, populares, banners y link de login
                .requestMatchers(HttpMethod.GET, "/api/inicio").permitAll()
                // [Pública] API de login: autenticación y emisión de token JWT
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                // [Pública] Búsqueda y listado resumido de eventos con filtros
                .requestMatchers(HttpMethod.GET, "/api/eventos").permitAll()

                // Recursos estáticos y vistas públicas
                .requestMatchers("/", "/eventos", "/login", "/registro", "/error",
                                 "/css/**", "/img/**", "/js/**").permitAll()

                // ==========================================
                // 2. APIs Privadas especificadas en la rúbrica:
                // ==========================================
                // [Privada] Detalle completo de evento (requiere token JWT en header Authorization)
                .requestMatchers(HttpMethod.GET, "/api/eventos/*").authenticated()
                .requestMatchers("/api/perfil/**").authenticated()

                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )

            // Soporte para login tradicional en navegador si se requiere
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/eventos", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            // Registrar el filtro JWT antes del filtro estándar de usuario y contraseña
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
