package duoc.eventogamer;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestExecutionListeners({
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
class EventogamerApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Contexto Spring Boot carga correctamente")
    void contextLoads() {
    }

    @Test
    @DisplayName("[API Pública] GET /api/inicio retorna eventos recientes, populares y banners")
    void apiInicioEsPublica() throws Exception {
        mockMvc.perform(get("/api/inicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventosRecientes", notNullValue()))
                .andExpect(jsonPath("$.eventosPopulares", notNullValue()))
                .andExpect(jsonPath("$.bannersAvisosComerciales", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.loginUrl", is("/api/auth/login")));
    }

    @Test
    @DisplayName("[API Pública] GET /api/eventos permite buscar y filtrar públicamente")
    void apiEventosBusquedaEsPublicaYFiltra() throws Exception {
        // Búsqueda general pública sin autenticación
        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].nombre", notNullValue()))
                .andExpect(jsonPath("$[0].videojuego", notNullValue()))
                .andExpect(jsonPath("$[0].modalidad", notNullValue()));

        // Filtro por videojuego 'Valorant'
        mockMvc.perform(get("/api/eventos").param("videojuego", "Valorant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].videojuego", is("Valorant")));

        // Filtro por modalidad 'Online'
        mockMvc.perform(get("/api/eventos").param("modalidad", "Online"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modalidad", is("Online")));
    }

    @Test
    @DisplayName("[API Pública] POST /api/auth/login falla con credenciales incorrectas")
    void apiLoginFallaCredencialesInvalidas() throws Exception {
        String badCredentialsJson = """
                {
                    "username": "usuario_inexistente",
                    "password": "PasswordInvalida123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCredentialsJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    @DisplayName("[API Pública] POST /api/auth/login genera y retorna Token JWT válido")
    void apiLoginExitosoRetornaJwt() throws Exception {
        String validCredentialsJson = """
                {
                    "username": "fernando",
                    "password": "Gamer2026!"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCredentialsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.username", is("fernando")))
                .andExpect(jsonPath("$.roles", hasItem("ROLE_USER")));
    }

    @Test
    @DisplayName("[API Privada] GET /api/eventos/{id} sin token JWT retorna 401 Unauthorized")
    void apiEventosDetalleSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Unauthorized")));
    }

    @Test
    @DisplayName("[API Privada] GET /api/eventos/{id} con token JWT válido retorna detalle completo")
    void apiEventosDetalleConTokenRetorna200YDetalleCompleto() throws Exception {
        // 1. Login para obtener token
        String loginJson = """
                {
                    "username": "gina",
                    "password": "Gamer2026!"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = JsonPath.read(responseBody, "$.token");

        // 2. Consumir API privada con token en cabecera Authorization
        mockMvc.perform(get("/api/eventos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Torneo Valorant Open Santiago")))
                .andExpect(jsonPath("$.videojuego", is("Valorant")))
                .andExpect(jsonPath("$.descripcion", notNullValue()))
                .andExpect(jsonPath("$.organizador", notNullValue()))
                .andExpect(jsonPath("$.requisitosParticipacion", notNullValue()))
                .andExpect(jsonPath("$.informacionInscripcion", notNullValue()))
                .andExpect(jsonPath("$.fotografias", hasSize(greaterThan(0))));
    }
}
