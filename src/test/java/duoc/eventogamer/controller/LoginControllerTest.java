package duoc.eventogamer.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginControllerTest {

    @Test
    @DisplayName("login retorna la vista login")
    void loginRetornaVistaLogin() {
        LoginController controller = new LoginController();
        assertEquals("login", controller.login());
    }
}
