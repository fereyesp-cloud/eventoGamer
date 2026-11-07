package duoc.eventogamer.config;

import duoc.eventogamer.jwt.JwtAuthenticationEntryPoint;
import duoc.eventogamer.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    // ==========================================
    // Cadena 1: API REST (/api/**) — sin estado, autenticación por JWT
    // ==========================================
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")

            // CSRF no aplica: la API no usa cookies, el token viaja en la cabecera Authorization
            .csrf(csrf -> csrf.disable())

            // Sin sesión de servidor: nunca se crea JSESSIONID para la API
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Las respuestas JSON no deben cargar recursos ni mostrarse en iframes
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'none'; frame-ancestors 'none'"))
            )

            // 401 en JSON cuando falta o es inválido el token
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))

            .authorizeHttpRequests(auth -> auth
                // [Pública] Login: emite el token JWT
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // [Pública] Inicio y búsqueda resumida de eventos
                .requestMatchers(HttpMethod.GET, "/api/inicio", "/api/eventos").permitAll()
                // [Privada] Detalle de eventos y todo lo demás requiere JWT
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ==========================================
    // Cadena 2: Páginas web (Thymeleaf) — sesión con login por formulario
    // ==========================================
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF activo: los formularios Thymeleaf incluyen el token con th:action

            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "script-src 'self'; " +
                    "style-src 'self'; " +
                    "img-src 'self' data:; " +
                    "font-src 'self'; " +
                    "connect-src 'self'; " +
                    "object-src 'none'; " +
                    "base-uri 'self'; " +
                    "form-action 'self'; " +
                    "frame-ancestors 'none'"
                ))
            )   

            .authorizeHttpRequests(auth -> auth
                // Páginas públicas y recursos estáticos
                .requestMatchers("/", "/eventos", "/login", "/registro", "/error",
                                 "/css/**", "/img/**", "/js/**", "/fonts/**").permitAll()
                // Todo lo demás (detalle de evento, perfil) requiere login
                .anyRequest().authenticated()
            )   

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/eventos", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
