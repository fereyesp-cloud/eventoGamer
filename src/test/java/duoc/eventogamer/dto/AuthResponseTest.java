package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthResponseTest {

    @Test
    @DisplayName("AuthResponse inicializa token, tipo Bearer, username, roles y expiración")
    void constructorYGetters() {
        AuthResponse res = new AuthResponse("jwt-123", "fernando", List.of("ROLE_USER"), 86400000L);
        assertEquals("jwt-123", res.getToken());
        assertEquals("Bearer", res.getType());
        assertEquals("fernando", res.getUsername());
        assertEquals(List.of("ROLE_USER"), res.getRoles());
        assertEquals(86400000L, res.getExpiresInMs());
    }
}
