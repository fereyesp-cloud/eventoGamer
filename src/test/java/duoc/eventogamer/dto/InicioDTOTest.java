package duoc.eventogamer.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InicioDTOTest {

    @Test
    @DisplayName("InicioDTO almacena recientes, populares, banners y loginUrl")
    void constructorYGetters() {
        InicioDTO dto = new InicioDTO(List.of(), List.of(), List.of("HyperGear"), "/api/auth/login");
        assertEquals(List.of("HyperGear"), dto.getBannersAvisosComerciales());
        assertEquals("/api/auth/login", dto.getLoginUrl());
    }
}
