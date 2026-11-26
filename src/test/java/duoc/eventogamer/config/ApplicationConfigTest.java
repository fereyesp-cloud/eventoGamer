package duoc.eventogamer.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationConfigTest {

    @Test
    @DisplayName("passwordEncoder entrega un BCryptPasswordEncoder funcional")
    void passwordEncoderCifraYVerifica() {
        ApplicationConfig config = new ApplicationConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        String hash = encoder.encode("ClaveSegura2026!");
        assertNotEquals("ClaveSegura2026!", hash);
        assertTrue(encoder.matches("ClaveSegura2026!", hash));
        assertFalse(encoder.matches("OtraClave", hash));
    }

    @Test
    @DisplayName("authenticationManager delega en AuthenticationConfiguration")
    void authenticationManagerDelega() throws Exception {
        ApplicationConfig config = new ApplicationConfig();
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager expected = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(expected);

        assertSame(expected, config.authenticationManager(authConfig));
    }
}
