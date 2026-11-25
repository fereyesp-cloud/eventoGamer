package duoc.eventogamer.controller.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    @Test
    @DisplayName("handleResponseStatus y handleMaxSize devuelven JSON limpio con código HTTP correcto")
    void manejaErroresEnJson() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<Map<String, Object>> r404 = handler.handleResponseStatus(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No encontrado"));
        assertEquals(404, r404.getStatusCode().value());
        assertEquals("No encontrado", r404.getBody().get("error"));

        ResponseEntity<Map<String, Object>> r413 = handler.handleMaxSize(
                new MaxUploadSizeExceededException(7L * 1024 * 1024));
        assertEquals(413, r413.getStatusCode().value());
        assertTrue(r413.getBody().get("error").toString().contains("7 MB"));
    }
}
