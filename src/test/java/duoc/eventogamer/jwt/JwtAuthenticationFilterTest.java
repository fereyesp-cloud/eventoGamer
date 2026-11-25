package duoc.eventogamer.jwt;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("doFilterInternal autentica en SecurityContext cuando recibe un Bearer JWT válido")
    void autenticaConTokenValido() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        UserDetails user = User.withUsername("fernando").password("hash").roles("USER").build();
        when(jwtService.extraerUsername("token-valido")).thenReturn("fernando");
        when(userDetailsService.loadUserByUsername("fernando")).thenReturn(user);
        when(jwtService.esTokenValido("token-valido", user)).thenReturn(true);

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/eventos/1");
        req.addHeader("Authorization", "Bearer token-valido");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("fernando", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(req, res);
    }
}
