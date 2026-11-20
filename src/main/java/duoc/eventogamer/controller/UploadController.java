package duoc.eventogamer.controller;

import duoc.eventogamer.service.MediaStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Controller
public class UploadController {

    private final MediaStorageService mediaStorageService;

    public UploadController(MediaStorageService mediaStorageService) {
        this.mediaStorageService = mediaStorageService;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> servirArchivo(@PathVariable String filename) {
        Resource resource = mediaStorageService.cargarArchivo(filename);
        String mimeType = mediaStorageService.detectarMimePorNombre(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePrivate())
                .body(resource);
    }
}
