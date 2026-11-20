package duoc.eventogamer.service;

import duoc.eventogamer.model.TipoMedia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MediaStorageService {

    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB

    private static final Set<String> FOTO_EXTENSIONES = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Set<String> VIDEO_EXTENSIONES = Set.of("mp4", "webm");

    private static final Set<String> FOTO_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );
    private static final Set<String> VIDEO_CONTENT_TYPES = Set.of(
            "video/mp4", "video/webm"
    );

    private static final Map<String, String> EXTENSION_A_MIME = Map.of(
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "webp", "image/webp",
            "gif", "image/gif",
            "mp4", "video/mp4",
            "webm", "video/webm"
    );

    private static final Pattern SAFE_LOCAL_PATH = Pattern.compile("^/(img|uploads)/[a-zA-Z0-9/_-]+\\.[a-zA-Z0-9]{2,5}$");
    private static final Pattern SAFE_FILENAME = Pattern.compile("^[a-zA-Z0-9_-]+\\.(jpg|jpeg|png|webp|gif|mp4|webm)$");
    private static final Pattern YOUTUBE_WATCH = Pattern.compile("(?:youtube\\.com/watch\\?.*v=|youtu\\.be/|youtube(?:-nocookie)?\\.com/embed/)([a-zA-Z0-9_-]{11})");
    private static final Pattern VIMEO_VIDEO = Pattern.compile("(?:player\\.)?vimeo\\.com/(?:video/)?(\\d{5,15})");

    private final Path uploadRoot;

    public MediaStorageService(@Value("${app.upload.dir:./uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String validarYNormalizarUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La URL multimedia es obligatoria");
        }

        String url = rawUrl.trim();
        if (url.length() > 500 || url.contains("..") || url.chars().anyMatch(Character::isISOControl)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La URL multimedia no es válida");
        }

        if (url.startsWith("/")) {
            if (!SAFE_LOCAL_PATH.matcher(url).matches()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ruta local multimedia no es válida");
            }
            return url;
        }

        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (scheme == null || !"https".equalsIgnoreCase(scheme) || host == null || host.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se permiten enlaces seguros HTTPS o rutas internas válidas");
            }
            return uri.toString();
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato de la URL no es válido");
        }
    }

    public String extraerEmbedUrl(String url, TipoMedia tipo) {
        if (tipo != TipoMedia.VIDEO || url == null) {
            return null;
        }

        Matcher ytMatcher = YOUTUBE_WATCH.matcher(url);
        if (ytMatcher.find()) {
            return "https://www.youtube-nocookie.com/embed/" + ytMatcher.group(1);
        }

        Matcher vimeoMatcher = VIMEO_VIDEO.matcher(url);
        if (vimeoMatcher.find()) {
            return "https://player.vimeo.com/video/" + vimeoMatcher.group(1);
        }

        return null;
    }

    public String guardarArchivo(MultipartFile archivo, TipoMedia tipo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes seleccionar un archivo para subir");
        }
        if (archivo.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo supera el tamaño máximo permitido de 10 MB");
        }
        if (tipo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar si el archivo es FOTO o VIDEO");
        }

        String extension = extraerExtension(archivo.getOriginalFilename());
        validarTipoArchivo(tipo, extension, archivo.getContentType());

        String nombreSeguro = UUID.randomUUID() + "." + extension;
        try {
            Files.createDirectories(uploadRoot);
            Path destino = uploadRoot.resolve(nombreSeguro).normalize();
            if (!destino.startsWith(uploadRoot)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruta de archivo inválida");
            }
            try (InputStream inputStream = archivo.getInputStream()) {
                Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/" + nombreSeguro;
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo almacenar el archivo multimedia");
        }
    }

    public Resource cargarArchivo(String filename) {
        if (filename == null || !SAFE_FILENAME.matcher(filename).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de archivo inválido");
        }
        Path archivo = uploadRoot.resolve(filename).normalize();
        if (!archivo.startsWith(uploadRoot) || !Files.isRegularFile(archivo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado");
        }
        try {
            return new UrlResource(archivo.toUri());
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no disponible");
        }
    }

    public String detectarMimePorNombre(String filename) {
        String ext = extraerExtension(filename);
        return EXTENSION_A_MIME.getOrDefault(ext, "application/octet-stream");
    }

    private String extraerExtension(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener una extensión válida");
        }
        String ext = nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if (ext.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extensión de archivo no válida");
        }
        return ext;
    }

    private void validarTipoArchivo(TipoMedia tipo, String extension, String contentType) {
        String mime = contentType != null ? contentType.toLowerCase(Locale.ROOT) : "";
        if (tipo == TipoMedia.FOTO) {
            if (!FOTO_EXTENSIONES.contains(extension) || !FOTO_CONTENT_TYPES.contains(mime)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Formato de foto no permitido. Usa JPG, PNG, WEBP o GIF");
            }
        } else {
            if (!VIDEO_EXTENSIONES.contains(extension) || !VIDEO_CONTENT_TYPES.contains(mime)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Formato de video no permitido. Usa MP4 o WEBM");
            }
        }
    }
}
