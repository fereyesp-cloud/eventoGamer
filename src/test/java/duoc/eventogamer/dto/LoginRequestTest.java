package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    @DisplayName("LoginRequest almacena username y password")
    void constructorYGetters() {
        LoginRequest req = new LoginRequest("fernando", "ClaveDemo");
        assertEquals("fernando", req.getUsername());
        assertEquals("ClaveDemo", req.getPassword());
    }
}
