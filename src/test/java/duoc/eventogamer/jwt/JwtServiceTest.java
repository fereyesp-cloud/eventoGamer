package duoc.eventogamer.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
    }

    @Test
    @DisplayName("generarToken crea un JWT válido con username y roles esperados")
    void generarYValidarToken() {
        UserDetails admin = User.withUsername("admin").password("hash").roles("USER", "ADMIN").build();

        String token = jwtService.generarToken(admin);

        assertNotNull(token);
        assertEquals("admin", jwtService.extraerUsername(token));
        List<String> roles = jwtService.extraerRoles(token);
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertTrue(jwtService.esTokenValido(token, admin));
        assertFalse(jwtService.esTokenExpirado(token));
        assertEquals(3600000L, jwtService.getJwtExpiration());
    }
}
