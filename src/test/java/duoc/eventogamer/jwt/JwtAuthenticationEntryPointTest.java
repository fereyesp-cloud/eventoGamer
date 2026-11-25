package duoc.eventogamer.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationEntryPointTest {

    @Test
    @DisplayName("commence retorna 401 JSON para /api/** y redirige a /login para rutas web")
    void commenceRespondeSegunTipoDeRuta() throws Exception {
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

        MockHttpServletRequest apiReq = new MockHttpServletRequest("GET", "/api/eventos/1");
        MockHttpServletResponse apiRes = new MockHttpServletResponse();
        entryPoint.commence(apiReq, apiRes, new BadCredentialsException("No auth"));

        assertEquals(401, apiRes.getStatus());
        assertTrue(apiRes.getContentAsString().contains("Unauthorized"));

        MockHttpServletRequest webReq = new MockHttpServletRequest("GET", "/eventos/1");
        MockHttpServletResponse webRes = new MockHttpServletResponse();
        entryPoint.commence(webReq, webRes, new BadCredentialsException("No auth"));

        assertEquals("/login", webRes.getRedirectedUrl());
    }
}
