package duoc.eventogamer.controller;

import duoc.eventogamer.service.MediaStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UploadControllerTest {

    @Test
    @DisplayName("servirArchivo entrega el recurso con cabecera X-Content-Type-Options nosniff")
    void servirArchivoConCabecerasSeguras() {
        MediaStorageService storage = mock(MediaStorageService.class);
        Resource res = new ByteArrayResource(new byte[]{1, 2, 3});
        when(storage.cargarArchivo("foto.png")).thenReturn(res);
        when(storage.detectarMimePorNombre("foto.png")).thenReturn("image/png");

        UploadController controller = new UploadController(storage);
        ResponseEntity<Resource> response = controller.servirArchivo("foto.png");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("nosniff", response.getHeaders().getFirst("X-Content-Type-Options"));
        assertSame(res, response.getBody());
    }
}
