package duoc.eventogamer.controller.api;

import duoc.eventogamer.dto.AuthResponse;
import duoc.eventogamer.dto.LoginRequest;
import duoc.eventogamer.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthApiControllerTest {

    @Test
    @DisplayName("login retorna 200 con JWT ante credenciales válidas y 401 ante credenciales inválidas")
    void loginValidoEInvalido() {
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        AuthApiController controller = new AuthApiController(authManager, jwtService);

        UserDetails user = User.withUsername("fernando").password("hash").roles("USER").build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generarToken(user)).thenReturn("jwt-demo");
        when(jwtService.getJwtExpiration()).thenReturn(86400000L);

        ResponseEntity<?> ok = controller.login(new LoginRequest("fernando", "ClaveOk"));
        assertEquals(200, ok.getStatusCode().value());
        assertEquals("jwt-demo", ((AuthResponse) ok.getBody()).getToken());

        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad"));
        ResponseEntity<?> fail = controller.login(new LoginRequest("fernando", "Mal"));
        assertEquals(401, fail.getStatusCode().value());
    }
}
