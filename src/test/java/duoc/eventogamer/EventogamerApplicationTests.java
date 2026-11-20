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
                .andExpect(jsonPath("$.fotografias", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.multimedia", notNullValue()));
    }

    @Test
    @DisplayName("[APIs Privadas] Multimedia, compartir, comentarios y valoraciones sin JWT retornan 401")
    void apisInteraccionSinTokenRetornan401() throws Exception {
        mockMvc.perform(get("/api/eventos/1/media"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/eventos/1/media")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"FOTO\",\"url\":\"https://ejemplo.com/a.jpg\"}"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/eventos/1/compartir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"canal\":\"SITIO_WEB\",\"mensaje\":\"Hola\"}"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/eventos/compartidos"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/eventos/1/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contenido\":\"Gran evento\"}"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/eventos/1/valoraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"puntuacion\":5}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[APIs Privadas] Con JWT permite agregar media, compartir, comentar y valorar")
    void apisInteraccionConTokenFuncionanCorrectamente() throws Exception {
        String loginJson = """
                {
                    "username": "fernando",
                    "password": "Gamer2026!"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
        String token = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");

        // 1. Agregar foto y video por URL
        mockMvc.perform(post("/api/eventos/1/media")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"VIDEO\",\"url\":\"https://youtu.be/e_E9W2vsRbQ\",\"titulo\":\"Resumen\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo", is("VIDEO")))
                .andExpect(jsonPath("$.embedUrl", is("https://www.youtube-nocookie.com/embed/e_E9W2vsRbQ")))
                .andExpect(jsonPath("$.usuario", is("fernando")));

        // 2. Compartir evento en sitio web y redes sociales
        mockMvc.perform(post("/api/eventos/1/compartir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"canal\":\"SITIO_WEB\",\"mensaje\":\"¡Imperdible torneo!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.canal", is("SITIO_WEB")))
                .andExpect(jsonPath("$.enlacesRedesSociales.whatsapp", notNullValue()))
                .andExpect(jsonPath("$.enlacesRedesSociales.twitter", notNullValue()));

        // 3. Comentar evento
        mockMvc.perform(post("/api/eventos/1/comentarios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contenido\":\"¡Excelente iniciativa para la comunidad gamer!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario", is("fernando")))
                .andExpect(jsonPath("$.contenido", containsString("comunidad gamer")));

        // 4. Valorar evento
        mockMvc.perform(post("/api/eventos/1/valoraciones")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"puntuacion\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.miValoracion", is(5)))
                .andExpect(jsonPath("$.promedio", greaterThan(0.0)));
    }
}
