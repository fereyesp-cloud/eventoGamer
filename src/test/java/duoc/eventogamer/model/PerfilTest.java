package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    @Test
    @DisplayName("Perfil inicializa valores por defecto y permite actualizar preferencias")
    void constructorYSetters() {
        Perfil perfil = new Perfil("gina");
        assertEquals("gina", perfil.getUsername());
        assertEquals("gina", perfil.getGamertag());
        assertEquals("🎮", perfil.getAvatar());
        assertTrue(perfil.isNotifNuevosEventos());

        perfil.setNombre("Gina");
        perfil.setCorreo("gina@duoc.cl");
        perfil.setJuegos(List.of("League of Legends"));
        perfil.setNotifCorreo(true);

        assertEquals("Gina", perfil.getNombre());
        assertEquals("gina@duoc.cl", perfil.getCorreo());
        assertEquals(1, perfil.getJuegos().size());
        assertTrue(perfil.isNotifCorreo());
    }
}
