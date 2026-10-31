package duoc.eventogamer.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (request.getRequestURI().startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = "{\"status\":401,\"error\":\"Unauthorized\","
                    + "\"message\":\"Acceso denegado: se requiere un token JWT válido en la cabecera 'Authorization: Bearer <token>'\","
                    + "\"path\":\"" + request.getRequestURI() + "\"}";

            response.getWriter().write(jsonResponse);
        } else {
            response.sendRedirect("/login");
        }
    }
}
