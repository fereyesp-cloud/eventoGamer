package duoc.eventogamer.service;

import duoc.eventogamer.model.TipoMedia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MediaStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("valida URLs HTTPS, guarda archivos permitidos y detecta MIME correctamente")
    void validaUrlYGuardaArchivo() {
        MediaStorageService storage = new MediaStorageService(tempDir.toString());

        assertEquals("https://ejemplo.com/foto.png", storage.validarYNormalizarUrl("https://ejemplo.com/foto.png"));
        assertThrows(ResponseStatusException.class, () -> storage.validarYNormalizarUrl("javascript:alert(1)"));

        MockMultipartFile file = new MockMultipartFile("archivo", "foto.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String url = storage.guardarArchivo(file, TipoMedia.FOTO);
        assertTrue(url.startsWith("/uploads/"));
        assertEquals("image/jpeg", storage.detectarMimePorNombre("demo.jpg"));
    }
}
