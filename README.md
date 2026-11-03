# 🎮 Eventos Gamer — API REST

Backend para una plataforma que reúne eventos de la comunidad gamer: torneos, LAN parties, exposiciones y meetups. Expone APIs REST protegidas con Spring Security + JWT, y todos los datos se obtienen desde una base de datos MySQL mediante Spring Data JPA.

# Proyecto desarrollado para la asignatura Seguridad y Calidad de Software — DUOC UC.

# 🛠️ Tecnologías
Tecnología	Versión
Java	21
Spring Boot	4.1.2
Spring Web, Spring Security	7
Spring Data JPA / Hibernate	7
MySQL Community Server	9.7 LTS
MySQL Driver (Connector/J)	gestionado por Spring Boot
JJWT	0.12.6
Maven (wrapper incluido)	—
# 🗄️ Base de datos
Requisitos
MySQL 9.7 LTS instalado localmente como servicio (puerto 3306).
Configuración recomendada en la instalación: Development Computer, sin abrir el puerto en el firewall (la app se conecta por localhost), sin bases de ejemplo.
1. Crear la base de datos y el usuario de la aplicación

# Ingresar como administrador (mysql -u root -p) y ejecutar:

sql
CREATE DATABASE eventogamer
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER 'eventogamer_app'@'localhost' IDENTIFIED BY 'CAMBIAR_ESTA_CLAVE';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
  ON eventogamer.* TO 'eventogamer_app'@'localhost';

Cada integrante define su propia contraseña en su MySQL local. La aplicación nunca se conecta con root.

2. Definir la contraseña como variable de entorno

La contraseña no se escribe en el código ni se sube a Git: la aplicación la lee desde la variable DB_PASSWORD.

cmd
setx DB_PASSWORD "la_clave_definida_en_el_paso_1"

Luego cerrar y volver a abrir VS Code (o la consola) para que tome la variable.

3. Tablas y datos iniciales

No es necesario ejecutar scripts de tablas ni de datos:

Hibernate crea las tablas automáticamente a partir de las entidades JPA (ddl-auto=update).
DataInitializer carga los datos iniciales al arrancar, solo si las tablas están vacías (reiniciar la app no duplica datos): 3 usuarios, 4 banners y 5 eventos.
Datos de conexión
Parámetro	Valor
Host	localhost
Puerto	3306
Base de datos	eventogamer
Usuario	eventogamer_app (configurable con la variable DB_USER)
Contraseña	variable de entorno DB_PASSWORD
Modelo de datos
Tabla	Contenido
evento	Datos principales del evento: nombre, descripción, videojuego, categoría, país, ciudad, modalidad, fecha, hora, ubicación o enlace, requisitos, inscripción, popularidad y fecha de publicación
evento_foto	Fotografías del evento (una fila por foto)
evento_organizador	Organizadores del evento
evento_expositor	Principales expositores del evento
banner	Avisos comerciales del inicio (texto, activo, orden)
usuario	Usuarios del sistema, con contraseña cifrada con BCrypt
usuario_rol	Roles de cada usuario (USER, ADMIN)

Índices definidos en evento: fecha, país + ciudad, modalidad y fecha de publicación, para acelerar la búsqueda y el orden de "recientes".

# ▶️ Cómo ejecutar

Con MySQL configurado según la sección anterior:

bash
# Windows
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run

La aplicación queda disponible en http://localhost:8080.

# 👤 Usuarios de prueba
Usuario	Contraseña	Roles
fernando	Gamer2026!	USER
gina	Gamer2026!	USER
admin	Admin2026!	USER, ADMIN

⚠️ Credenciales solo para demostración académica. En la base se almacenan como hash BCrypt, nunca en texto plano.

# 🔌 APIs
Públicas
Método	Ruta	Descripción
POST	/api/auth/login	Recibe usuario y contraseña, retorna un token JWT
GET	/api/inicio	Eventos más recientes, eventos populares, banners comerciales y enlace al login
GET	/api/eventos	Búsqueda de eventos con información resumida

Filtros opcionales de /api/eventos (se pueden combinar):

Parámetro	Ejemplo	Tipo de búsqueda
nombre	?nombre=torneo	Parcial, sin distinguir mayúsculas
videojuego	?videojuego=valo	Parcial
categoria o tipo	?categoria=Torneo	Exacta
paisCiudad	?paisCiudad=santiago	Parcial en país, ciudad o ubicación
fecha	?fecha=2026-11-14	Exacta (yyyy-MM-dd)
modalidad	?modalidad=online	Presencial u Online
Privadas (requieren token JWT)
Método	Ruta	Descripción
GET	/api/eventos/{id}	Detalle completo: descripción, videojuego, fecha y hora, ubicación o enlace, organizador, requisitos, información de inscripción y fotografías

El token se envía en la cabecera:

Authorization: Bearer <token>
Ejemplos con curl

Git Bash / Linux / macOS

bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"fernando","password":"Gamer2026!"}'

# Detalle privado
curl http://localhost:8080/api/eventos/1 \
  -H "Authorization: Bearer <token>"

cmd (Windows)

cmd
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"fernando\",\"password\":\"Gamer2026!\"}"

curl http://localhost:8080/api/eventos/1 -H "Authorization: Bearer <token>"
# 📁 Estructura del proyecto
src/main/java/duoc/eventogamer/
├── config/
│   ├── ApplicationConfig.java      # PasswordEncoder y AuthenticationManager
│   ├── DataInitializer.java        # Carga inicial de usuarios, banners y eventos
│   └── SecurityConfig.java         # Rutas públicas/privadas y filtro JWT
├── controller/
│   └── api/
│       ├── AuthApiController.java  # POST /api/auth/login
│       ├── EventoApiController.java# Búsqueda y detalle de eventos
│       └── InicioApiController.java# Datos de la página de inicio
├── dto/                            # Objetos de respuesta (resumen, detalle, inicio, login)
├── jwt/                            # Generación, validación y filtro de tokens JWT
├── model/
│   ├── Banner.java                 # Entidad JPA
│   ├── Evento.java                 # Entidad JPA
│   └── Usuario.java                # Entidad JPA
├── repository/
│   ├── BannerRepository.java
│   ├── EventoRepository.java       # Incluye la consulta de búsqueda con filtros
│   └── UsuarioRepository.java
└── service/
    ├── EventoService.java          # Lógica de inicio, búsqueda y detalle
    └── UsuarioDetailsService.java  # Carga de usuarios desde MySQL para Spring Security
# 🔒 Medidas de seguridad
Base de datos
Mínimo privilegio: la app usa un usuario exclusivo (eventogamer_app), con permisos solo sobre su base y sin DROP ni GRANT. Nunca se conecta con root.
Acceso solo local: el usuario está restringido a localhost y el puerto 3306 no está abierto en el firewall.
Credenciales fuera del código: la contraseña de la base se lee desde la variable de entorno DB_PASSWORD.
Contraseñas cifradas con BCrypt: los usuarios se guardan con hash, nunca en texto plano. Las contraseñas nunca se registran en los logs.
Prevención de inyección SQL: todas las consultas usan JPA con parámetros preparados; los valores ingresados por el usuario nunca se concatenan al SQL.
Transacciones de solo lectura en las consultas de eventos.
Rol fijo en el registro: los usuarios nuevos reciben siempre el rol USER; el rol nunca proviene del formulario.
API
Autenticación sin estado con JWT: las APIs privadas exigen un token válido en la cabecera Authorization.
CSRF deshabilitado solo para /api/**: al usar JWT en la cabecera (no cookies), las APIs no son vulnerables a CSRF.
DTOs: la búsqueda pública retorna un resumen; el detalle completo solo se entrega con autenticación. Las entidades nunca se exponen directamente.
Mensaje de login genérico: la respuesta es idéntica si el usuario no existe o si la contraseña es incorrecta (evita enumeración de usuarios).
Sin exposición de detalles internos: las respuestas de error no incluyen stack traces ni mensajes de excepción.
# ✅ Pruebas realizadas
Prueba	Resultado esperado	Estado
Login con credenciales válidas	200 + token JWT	✓
Detalle de evento con token	200 + datos y fotografías desde MySQL	✓
Detalle de evento sin token	401	✓
Detalle con token falso	401	✓
Detalle de evento inexistente	404, sin detalles internos	✓
Login con contraseña incorrecta	401	✓
Login con usuario inexistente	401, mismo mensaje que el caso anterior	✓
Búsqueda con filtros (modalidad, ciudad, videojuego parcial)	Resultados filtrados correctamente	✓
Filtro vacío (?nombre=)	Se ignora y retorna todos los eventos	✓
Reinicio de la aplicación	No duplica los datos iniciales	✓
# 📝 Notas
Workbench 26.x y fechas: la nueva versión de MySQL Workbench muestra las columnas DATE un día antes por conversión de zona horaria. Los datos están guardados correctamente; para verificar fechas usar la consola: SELECT fecha FROM evento;.
ddl-auto=update es adecuado para desarrollo. En producción las tablas se crearían con scripts versionados y al usuario de la app se le quitarían los permisos CREATE y ALTER.
Clave JWT: se mantiene en application.properties siguiendo el ejemplo del curso. Como mejora identificada, en producción debería inyectarse mediante una variable de entorno y rotarse.
👥 Integrantes
# Integrante	Responsabilidad
Fernando Reyes Pereira	- Ginna Gaggero