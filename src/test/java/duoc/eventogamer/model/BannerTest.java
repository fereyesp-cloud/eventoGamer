package duoc.eventogamer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BannerTest {

    @Test
    @DisplayName("Banner almacena texto, orden y estado activo")
    void constructorYGettersSetters() {
        Banner banner = new Banner("Promo HyperGear", 2);
        banner.setId(5L);
        assertEquals(5L, banner.getId());
        assertEquals("Promo HyperGear", banner.getTexto());
        assertEquals(2, banner.getOrden());
        assertTrue(banner.isActivo());

        banner.setActivo(false);
        assertFalse(banner.isActivo());
    }
}
