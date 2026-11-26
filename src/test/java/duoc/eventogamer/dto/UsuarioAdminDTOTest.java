package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioAdminDTOTest {

    @Test
    @DisplayName("UsuarioAdminDTO calcula automáticamente si el usuario es administrador")
    void calculaRolAdministrador() {
        UsuarioAdminDTO adminDto = new UsuarioAdminDTO(1L, "admin", true, List.of("ADMIN", "USER"));
        UsuarioAdminDTO userDto = new UsuarioAdminDTO(2L, "fernando", true, List.of("USER"));

        assertTrue(adminDto.isAdministrador());
        assertFalse(userDto.isAdministrador());
        assertEquals("admin", adminDto.getUsername());
    }
}
