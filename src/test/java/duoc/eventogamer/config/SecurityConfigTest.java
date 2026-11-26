package duoc.eventogamer.config;

import duoc.eventogamer.jwt.JwtAuthenticationEntryPoint;
import duoc.eventogamer.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    @DisplayName("SecurityConfig se instancia con filtro JWT y define el orden correcto de cadenas API (1) y Web (2)")
    void verificaOrdenYConfiguracionDeCadenas() throws NoSuchMethodException {
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        JwtAuthenticationEntryPoint entryPoint = mock(JwtAuthenticationEntryPoint.class);

        SecurityConfig config = new SecurityConfig(jwtFilter, entryPoint);
        assertNotNull(config);

        Method apiMethod = SecurityConfig.class.getMethod("apiSecurityFilterChain", HttpSecurity.class);
        Method webMethod = SecurityConfig.class.getMethod("webSecurityFilterChain", HttpSecurity.class);

        Order apiOrder = apiMethod.getAnnotation(Order.class);
        Order webOrder = webMethod.getAnnotation(Order.class);

        assertNotNull(apiOrder);
        assertNotNull(webOrder);
        assertEquals(1, apiOrder.value());
        assertEquals(2, webOrder.value());
    }
}
