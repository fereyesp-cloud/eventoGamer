# 🎮 Eventos Gamer

Aplicación web para reunir y difundir eventos de la comunidad gamer: torneos, LAN parties, exposiciones y meetups. Los visitantes pueden ver los próximos eventos y registrarse; los usuarios autenticados pueden buscar eventos, revisar su detalle y personalizar su perfil.

# Proyecto desarrollado para la asignatura Seguridad y Calidad de Software — DUOC UC.

# 🛠️ Tecnologías
Tecnología	Versión
Java	21
Spring Boot	4.1.2
Spring Security	7
Thymeleaf + Thymeleaf Extras Spring Security	3.1
Jakarta Bean Validation	—
Maven (wrapper incluido)	—
▶️ Cómo ejecutar

# Requisitos: JDK 21 o superior.

bash
# Windows
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run

Luego abrir en el navegador: http://localhost:8080

# 👤 Usuarios de prueba
Usuario	Contraseña	Rol
fernando	Gamer2026!	USER
gina	Gamer2026!	USER
admin	Admin2026!	USER, ADMIN

También es posible crear usuarios nuevos desde la página de registro.

# ⚠️ Credenciales solo para demostración académica. Los datos se almacenan en memoria y se pierden al reiniciar la aplicación.

🗺️ Rutas
Públicas
Ruta	Descripción
/	Inicio: eventos destacados, próximos eventos y marcas asociadas
/registro	Registro de usuario (nombre, correo, usuario y contraseña)
/login	Inicio de sesión
Privadas (requieren autenticación)
Ruta	Descripción
/eventos	Listado de eventos con filtros por categoría, fecha y ubicación
/eventos/{id}	Detalle del evento: descripción, dirección, fecha y hora, organizadores, servicios y expositores
/perfil	Perfil del usuario: gamertag, avatar, juegos de interés y notificaciones

Cualquier ruta no listada como pública requiere autenticación por defecto.

# 📁 Estructura del proyecto
src/main/java/duoc/eventogamer/
├── config/
│   └── SecurityConfig.java        # Reglas de acceso, login, logout y usuarios
├── controller/
│   ├── HomeController.java        # Página de inicio
│   ├── LoginController.java       # Vista de login
│   ├── RegistroController.java    # Registro de usuarios
│   ├── EventoController.java      # Listado, filtros y detalle de eventos
│   └── PerfilController.java      # Ver y editar perfil
├── model/
│   ├── Evento.java
│   ├── Perfil.java
│   ├── PerfilForm.java            # Solo campos editables del perfil
│   └── RegistroForm.java          # Datos y validaciones del registro
├── service/
│   ├── EventoService.java         # Eventos de ejemplo en memoria
│   └── PerfilService.java         # Perfiles en memoria y opciones válidas
└── EventogamerApplication.java

src/main/resources/
├── templates/
│   ├── fragments/header.html      # Header común reutilizable
│   ├── index.html
│   ├── login.html
│   ├── registro.html
│   ├── eventos.html
│   ├── evento-detalle.html
│   └── perfil.html
├── static/css/styles.css
└── application.properties

# 🔒 Medidas de seguridad implementadas
Protección de URLs por lista blanca: solo las rutas públicas se declaran explícitamente; todo lo demás exige autenticación.
Contraseñas cifradas con BCrypt: nunca se almacenan en texto plano.
Protección CSRF activa: los formularios usan th:action, que agrega el token automáticamente. El logout se realiza mediante POST.
Validación en el servidor: todos los formularios se validan con Bean Validation (@Valid), independiente de la validación del navegador.
Listas blancas de valores: el avatar y los juegos del perfil se validan contra las opciones permitidas.
Prevención de mass assignment: los formularios usan clases dedicadas (RegistroForm, PerfilForm) que contienen solo los campos editables.
Prevención de IDOR: el perfil se obtiene desde la sesión autenticada, nunca desde un identificador en la URL.
Prevención de XSS: las vistas usan th:text, que escapa el contenido HTML.
Mensaje de login genérico: no revela si el usuario existe o no (evita enumeración de usuarios).
Sin exposición de detalles internos: las páginas de error no muestran stack traces ni mensajes de excepción.
Patrón Post/Redirect/Get: evita reenvíos duplicados de formularios.

# 📝 Notas
Los eventos, usuarios registrados y perfiles se almacenan en memoria; no se utiliza base de datos en esta entrega.
El avatar se selecciona desde una lista predefinida en lugar de subir archivos, evitando los riesgos asociados a la carga de archivos.
# 👥 Integrantes
Fernando Reyes Pereira - Gina Gaggero Schivo