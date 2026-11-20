-- =====================================================================
--  EVENTOS GAMER - Script de creación e inserción de datos
--  Asignatura: Seguridad y Calidad de Software - DUOC UC
--  Motor: MySQL 9.7 LTS (compatible con MySQL 8.4+)
-- =====================================================================
--
--  Cómo ejecutarlo (como administrador de MySQL):
--
--      mysql -u root -p < db/eventogamer.sql
--
--  ¡ATENCIÓN! El script ELIMINA y vuelve a crear las tablas: los datos
--  existentes en la base `eventogamer` se pierden.
--
--  Antes de ejecutarlo, reemplace CAMBIAR_ESTA_CLAVE (sección 1) por una
--  contraseña segura y propia. Esa misma contraseña se define en la
--  variable de entorno DB_PASSWORD que lee la aplicación.
--
--  Nota: si la base está vacía, la aplicación también crea las tablas
--  (Hibernate, ddl-auto=update) y carga estos mismos datos al iniciar
--  (DataInitializer). Este script permite preparar la base sin levantar
--  la aplicación y deja documentado el modelo de datos.
-- =====================================================================

SET NAMES utf8mb4;


-- =====================================================================
--  1. BASE DE DATOS Y USUARIO DE LA APLICACIÓN
-- =====================================================================

CREATE DATABASE IF NOT EXISTS eventogamer
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- Usuario exclusivo de la aplicación (principio de mínimo privilegio):
--   - Solo puede conectarse desde el mismo equipo ('localhost').
--   - Solo tiene permisos sobre la base `eventogamer`.
--   - No tiene DROP (no puede borrar tablas) ni GRANT (no puede dar permisos).
CREATE USER IF NOT EXISTS 'eventogamer_app'@'localhost'
    IDENTIFIED BY 'CAMBIAR_ESTA_CLAVE';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON eventogamer.* TO 'eventogamer_app'@'localhost';

-- En producción, cuando las tablas se creen solo con este script, se
-- recomienda dejar únicamente SELECT, INSERT, UPDATE y DELETE:
-- REVOKE CREATE, ALTER, INDEX, REFERENCES ON eventogamer.* FROM 'eventogamer_app'@'localhost';

USE eventogamer;


-- =====================================================================
--  2. ELIMINACIÓN DE TABLAS EXISTENTES
-- =====================================================================
-- Se eliminan primero las tablas dependientes (con claves foráneas).

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS evento_valoracion;
DROP TABLE IF EXISTS evento_comentario;
DROP TABLE IF EXISTS evento_compartido;
DROP TABLE IF EXISTS evento_media;
DROP TABLE IF EXISTS usuario_rol;
DROP TABLE IF EXISTS evento_foto;
DROP TABLE IF EXISTS evento_expositor;
DROP TABLE IF EXISTS evento_organizador;
DROP TABLE IF EXISTS evento;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS banner;

SET FOREIGN_KEY_CHECKS = 1;


-- =====================================================================
--  3. CREACIÓN DE TABLAS
-- =====================================================================

-- ---------------------------------------------------------------------
-- 3.1 banner: avisos comerciales de la página de inicio
-- ---------------------------------------------------------------------
CREATE TABLE banner (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    texto   VARCHAR(200) NOT NULL,
    activo  BIT(1)       NOT NULL,          -- permite ocultar un banner sin borrarlo
    orden   INT          NOT NULL,          -- orden de aparición
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.2 usuario: usuarios que pueden iniciar sesión
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    username  VARCHAR(50)  NOT NULL,
    password  VARCHAR(100) NOT NULL,   -- SIEMPRE hash BCrypt, nunca texto plano
    activo    BIT(1)       NOT NULL,   -- un usuario inactivo no puede iniciar sesión
    PRIMARY KEY (id),
    UNIQUE KEY UK863n1y3x0jalatoir4325ehal (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.3 usuario_rol: roles de cada usuario (un usuario puede tener varios)
-- ---------------------------------------------------------------------
CREATE TABLE usuario_rol (
    usuario_id  BIGINT      NOT NULL,
    rol         VARCHAR(20) DEFAULT NULL,
    UNIQUE KEY UKhs0mqr8q0tsmf7gyfbp03qibu (usuario_id, rol),
    CONSTRAINT FKbyfgloj439r9wr9smrms9u33r
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.4 evento: datos principales de cada evento
-- ---------------------------------------------------------------------
CREATE TABLE evento (
    id                        BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                    VARCHAR(150) NOT NULL,
    descripcion               TEXT         NOT NULL,
    videojuego                VARCHAR(100) NOT NULL,
    categoria                 VARCHAR(60)  NOT NULL,
    modalidad                 VARCHAR(20)  NOT NULL,   -- 'Presencial' u 'Online'
    pais                      VARCHAR(60)  DEFAULT NULL,
    ciudad                    VARCHAR(80)  DEFAULT NULL,
    ubicacion                 VARCHAR(150) DEFAULT NULL,
    direccion                 VARCHAR(200) DEFAULT NULL,   -- eventos presenciales
    enlace_acceso             VARCHAR(300) DEFAULT NULL,   -- eventos online
    fecha                     DATE         NOT NULL,
    hora                      TIME         NOT NULL,
    organizador               VARCHAR(120) DEFAULT NULL,
    requisitos_participacion  TEXT,
    informacion_inscripcion   TEXT,
    foto_principal            VARCHAR(300) DEFAULT NULL,
    estacionamiento           BIT(1)       NOT NULL,
    locomocion_colectiva      BIT(1)       DEFAULT NULL,
    servicio_comida           BIT(1)       DEFAULT NULL,
    alojamiento               BIT(1)       NOT NULL,
    destacado                 BIT(1)       NOT NULL,
    popular                   BIT(1)       NOT NULL,   -- usado para "eventos populares"
    fecha_publicacion         DATE         NOT NULL,   -- usado para "eventos recientes"
    usuario_id                BIGINT       DEFAULT NULL, -- usuario que publicó el evento
    PRIMARY KEY (id),
    KEY idx_evento_fecha       (fecha),
    KEY idx_evento_ubicacion   (pais, ciudad),
    KEY idx_evento_modalidad   (modalidad),
    KEY idx_evento_publicacion (fecha_publicacion),
    KEY fk_evento_usuario      (usuario_id),
    CONSTRAINT fk_evento_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.5 evento_organizador: organizadores de cada evento (uno por fila)
-- ---------------------------------------------------------------------
CREATE TABLE evento_organizador (
    evento_id  BIGINT       NOT NULL,
    nombre     VARCHAR(120) DEFAULT NULL,
    KEY FKb6mwner45mn1byd1tsgl4vk52 (evento_id),
    CONSTRAINT FKb6mwner45mn1byd1tsgl4vk52
        FOREIGN KEY (evento_id) REFERENCES evento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.6 evento_expositor: principales expositores de cada evento
-- ---------------------------------------------------------------------
CREATE TABLE evento_expositor (
    evento_id  BIGINT       NOT NULL,
    nombre     VARCHAR(120) DEFAULT NULL,
    KEY FKda830126tgfhnimy01hm10o97 (evento_id),
    CONSTRAINT FKda830126tgfhnimy01hm10o97
        FOREIGN KEY (evento_id) REFERENCES evento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.7 evento_foto: fotografías iniciales de cada evento
-- ---------------------------------------------------------------------
CREATE TABLE evento_foto (
    evento_id  BIGINT       NOT NULL,
    url        VARCHAR(300) DEFAULT NULL,
    KEY FK5atsqsapy427i5jh56y4br6m8 (evento_id),
    CONSTRAINT FK5atsqsapy427i5jh56y4br6m8
        FOREIGN KEY (evento_id) REFERENCES evento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.8 evento_media: fotos y videos agregados a los eventos publicados
-- ---------------------------------------------------------------------
CREATE TABLE evento_media (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    evento_id       BIGINT       NOT NULL,
    usuario_id      BIGINT       NOT NULL,
    tipo            VARCHAR(10)  NOT NULL,   -- 'FOTO' o 'VIDEO'
    url             VARCHAR(500) NOT NULL,
    titulo          VARCHAR(120) DEFAULT NULL,
    fecha_creacion  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_media_evento  (evento_id),
    KEY idx_media_usuario (usuario_id),
    CONSTRAINT fk_media_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_media_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.9 evento_compartido: eventos compartidos en sitio web y redes sociales
-- ---------------------------------------------------------------------
CREATE TABLE evento_compartido (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    evento_id         BIGINT       NOT NULL,
    usuario_id        BIGINT       NOT NULL,
    canal             VARCHAR(20)  NOT NULL, -- 'SITIO_WEB', 'WHATSAPP', 'TWITTER', 'FACEBOOK', 'LINKEDIN'
    mensaje           VARCHAR(300) DEFAULT NULL,
    enlace_compartido VARCHAR(500) NOT NULL,
    fecha_compartido  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_compartido_evento      (evento_id),
    KEY idx_compartido_canal_fecha (canal, fecha_compartido),
    KEY fk_compartido_usuario      (usuario_id),
    CONSTRAINT fk_compartido_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_compartido_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.10 evento_comentario: comentarios de usuarios en eventos publicados
-- ---------------------------------------------------------------------
CREATE TABLE evento_comentario (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    evento_id       BIGINT        NOT NULL,
    usuario_id      BIGINT        NOT NULL,
    contenido       VARCHAR(1000) NOT NULL,
    fecha_creacion  DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    KEY idx_comentario_evento_fecha (evento_id, fecha_creacion),
    KEY fk_comentario_usuario       (usuario_id),
    CONSTRAINT fk_comentario_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_comentario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ---------------------------------------------------------------------
-- 3.11 evento_valoracion: valoraciones (1 a 5 estrellas) por evento y usuario
-- ---------------------------------------------------------------------
CREATE TABLE evento_valoracion (
    id                  BIGINT      NOT NULL AUTO_INCREMENT,
    evento_id           BIGINT      NOT NULL,
    usuario_id          BIGINT      NOT NULL,
    puntuacion          INT         NOT NULL,
    fecha_actualizacion DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_valoracion_evento_usuario (evento_id, usuario_id),
    KEY fk_valoracion_usuario (usuario_id),
    CONSTRAINT fk_valoracion_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id),
    CONSTRAINT fk_valoracion_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- =====================================================================
--  4. INSERCIÓN DE DATOS
-- =====================================================================

-- ---------------------------------------------------------------------
-- 4.1 Banners comerciales
-- ---------------------------------------------------------------------
INSERT INTO banner (id, texto, activo, orden) VALUES
    (1, 'HyperGear - 20% OFF en periféricos mecánicos con el cupón GAMER2026', b'1', 1),
    (2, 'NovaChair - Sillas ergonómicas Pro Series para torneos oficiales',     b'1', 2),
    (3, 'PixelNet Fibra - Conexión gamer ultra baja latencia 1 Gbps simétrico', b'1', 3),
    (4, 'EnergyDrink Monster - Patrocinador oficial del campeonato regional',   b'1', 4);

-- ---------------------------------------------------------------------
-- 4.2 Usuarios
-- ---------------------------------------------------------------------
-- Las contraseñas se almacenan como hash BCrypt (factor de costo 10).
-- Nunca se guardan en texto plano. Las credenciales de demo están en el README.
INSERT INTO usuario (id, username, password, activo) VALUES
    (1, 'fernando', '$2a$10$XxLJXSHfwRFFtyN1oMDpIOEFHoGxMNrwLznEMB3RGGZKOjQOsD74u', b'1'),
    (2, 'gina',     '$2a$10$H2snBk9k9AHjzsF3zhTz4uYfk9r9BsoG0Uq7TW6HH4OSnqGVG0qke', b'1'),
    (3, 'admin',    '$2a$10$cJ4HMJDA5BCPS742D7qR1e./gYWYY0MO5T.v7.QoKBRucl5val0H6', b'1');

-- ---------------------------------------------------------------------
-- 4.3 Roles de cada usuario
-- ---------------------------------------------------------------------
INSERT INTO usuario_rol (usuario_id, rol) VALUES
    (1, 'USER'),
    (2, 'USER'),
    (3, 'USER'),
    (3, 'ADMIN');

-- ---------------------------------------------------------------------
-- 4.4 Eventos
-- ---------------------------------------------------------------------
INSERT INTO evento (
    id, nombre, descripcion, videojuego, categoria, modalidad,
    pais, ciudad, ubicacion, direccion, enlace_acceso,
    fecha, hora, organizador, requisitos_participacion, informacion_inscripcion,
    foto_principal, estacionamiento, locomocion_colectiva, servicio_comida, alojamiento,
    destacado, popular, fecha_publicacion, usuario_id
) VALUES
    (1, 'Torneo Valorant Open Santiago',
        'Torneo 5v5 abierto de Valorant con premios en efectivo y transmisión en vivo por Twitch oficial.',
        'Valorant', 'Torneo', 'Presencial',
        'Chile', 'Santiago', 'Santiago', 'Av. Libertador Bernardo O''Higgins 3349, Estación Central', NULL,
        '2026-11-14', '10:00:00', 'Gamer Chile Org',
        'Mayor de 16 años, rango mínimo Platino en temporada vigente, periféricos propios permitidos.',
        'Inscripciones abiertas hasta el 10 de Noviembre. Cupos limitados a 16 escuadras.',
        '/img/eventos/valorant-1.jpg', b'1', b'1', b'1', b'0',
        b'1', b'1', '2026-10-20', 1),

    (2, 'Copa League of Legends Online',
        'Competencia clasificatoria 5v5 en Grieta del Invocador con casters oficiales y soporte vía Discord.',
        'League of Legends', 'Torneo', 'Online',
        'Chile', 'Online', 'Online', 'Servidor de Discord Oficial', 'https://discord.gg/lol-chile-cup',
        '2026-11-20', '18:00:00', 'Liga Gamer Online',
        'Residencia en Latinoamérica Sur (LAS), cuenta nivel 30+ sin sanciones de conducta.',
        'Inscripción gratuita mediante formulario oficial. Cierre 2 días antes de la primera ronda.',
        '/img/eventos/lol-online.jpg', b'0', b'0', b'0', b'0',
        b'1', b'1', '2026-10-22', 2),

    (3, 'Retro Gaming Fest Valparaíso',
        'Exhibición de consolas clásicas, free play, zona arcade y torneo relámpago de Street Fighter.',
        'Street Fighter 6', 'Exposición', 'Presencial',
        'Chile', 'Valparaíso', 'Valparaíso', 'Av. Brasil 1786, Valparaíso', NULL,
        '2026-11-28', '12:00:00', 'Club Retro Valpo',
        'Entrada liberada para todo público. Inscripción in situ para torneo relámpago.',
        'Adhesión voluntaria en puerta. Cupos de torneo por orden de llegada.',
        '/img/eventos/retro-fest.jpg', b'0', b'1', b'1', b'0',
        b'0', b'0', '2026-10-15', 3),

    (4, 'LAN Party Concepción 48 Horas',
        'Dos días seguidos de juego ininterrumpido en red local. Trae tu equipo (BYOC) o arrienda estación.',
        'Counter-Strike 2', 'LAN Party', 'Presencial',
        'Chile', 'Concepción', 'Concepción', 'Calle Barros Arana 1068, Concepción', NULL,
        '2026-12-05', '18:00:00', 'LAN Sur Producciones',
        'Ser mayor de 18 años o contar con autorización notarial. Pase de movilidad al día.',
        'Ticket participante BYOC $15.000 incluye puesto de red y credencial personalizada.',
        '/img/eventos/lan-party.jpg', b'1', b'1', b'1', b'1',
        b'1', b'1', '2026-10-25', 1),

    (5, 'Meetup Desarrolladores Indie Games',
        'Encuentro con estudios chilenos, muestras jugables de prototipos y paneles sobre motores Unreal y Unity.',
        'Varios / Indie', 'Meetup', 'Presencial',
        'Chile', 'Santiago', 'Santiago', 'Av. Vicuña Mackenna 4860, Macul', NULL,
        '2026-12-12', '16:30:00', 'VG Chile',
        'Abierto a estudiantes, desarrolladores y público general entusiasta de la industria.',
        'Registro previo por Passline para control de aforo.',
        '/img/eventos/indie-meetup.jpg', b'1', b'1', b'0', b'0',
        b'0', b'0', '2026-10-18', 2);

-- ---------------------------------------------------------------------
-- 4.5 Organizadores de cada evento
-- ---------------------------------------------------------------------
INSERT INTO evento_organizador (evento_id, nombre) VALUES
    (1, 'Gamer Chile Org'),
    (1, 'Riot Community CL'),
    (2, 'Liga Gamer Online'),
    (3, 'Club Retro Valpo'),
    (4, 'LAN Sur Producciones'),
    (5, 'Comunidad Indie CL'),
    (5, 'VG Chile');

-- ---------------------------------------------------------------------
-- 4.6 Expositores de cada evento
-- ---------------------------------------------------------------------
INSERT INTO evento_expositor (evento_id, nombre) VALUES
    (1, 'KRÜ Esports Academy'),
    (1, 'Caster Nacional'),
    (2, 'Streamers invitados'),
    (3, 'Coleccionistas Nacionales'),
    (3, 'Comunidad Arcade'),
    (4, 'Streamers locales'),
    (4, 'Creadores de contenido'),
    (5, 'Estudios Indie invitados'),
    (5, 'Profesores de desarrollo');

-- ---------------------------------------------------------------------
-- 4.7 Fotografías iniciales de cada evento
-- ---------------------------------------------------------------------
INSERT INTO evento_foto (evento_id, url) VALUES
    (1, '/img/eventos/valorant-1.jpg'),
    (1, '/img/eventos/valorant-arena.jpg'),
    (2, '/img/eventos/lol-online.jpg'),
    (3, '/img/eventos/retro-fest.jpg'),
    (4, '/img/eventos/lan-party.jpg'),
    (5, '/img/eventos/indie-meetup.jpg');

-- ---------------------------------------------------------------------
-- 4.8 Multimedia (fotos y videos) agregada a eventos
-- ---------------------------------------------------------------------
INSERT INTO evento_media (id, evento_id, usuario_id, tipo, url, titulo, fecha_creacion) VALUES
    (1, 1, 1, 'VIDEO', 'https://www.youtube.com/watch?v=e_E9W2vsRbQ', 'Tráiler oficial de la temporada Valorant', '2026-10-21 15:00:00'),
    (2, 2, 2, 'VIDEO', 'https://www.youtube.com/watch?v=aR-KAldshAE', 'Cinemática clasificatoria League of Legends', '2026-10-23 19:30:00');

-- ---------------------------------------------------------------------
-- 4.9 Comentarios de eventos
-- ---------------------------------------------------------------------
INSERT INTO evento_comentario (id, evento_id, usuario_id, contenido, fecha_creacion) VALUES
    (1, 1, 2, '¡Excelente organización el año pasado! Ya armamos nuestra escuadra para participar.', '2026-10-22 11:15:00'),
    (2, 1, 3, 'Recuerden llegar 30 minutos antes para validar periféricos y check-in.', '2026-10-22 14:40:00'),
    (3, 2, 1, 'Buenísimo que sea online con casters en vivo, nos vemos en Discord.', '2026-10-24 20:10:00');

-- ---------------------------------------------------------------------
-- 4.10 Valoraciones de eventos
-- ---------------------------------------------------------------------
INSERT INTO evento_valoracion (id, evento_id, usuario_id, puntuacion, fecha_actualizacion) VALUES
    (1, 1, 1, 5, '2026-10-22 10:00:00'),
    (2, 1, 2, 4, '2026-10-22 11:20:00'),
    (3, 2, 2, 5, '2026-10-24 18:00:00');

-- ---------------------------------------------------------------------
-- 4.11 Eventos compartidos en el sitio web
-- ---------------------------------------------------------------------
INSERT INTO evento_compartido (id, evento_id, usuario_id, canal, mensaje, enlace_compartido, fecha_compartido) VALUES
    (1, 1, 1, 'SITIO_WEB', '¡Quedan pocos cupos para el Torneo Valorant Open Santiago! Súmate con tu equipo.', 'http://localhost:8080/eventos/1', '2026-10-22 12:00:00'),
    (2, 2, 2, 'SITIO_WEB', 'Inscripción gratuita para la Copa LoL Online. ¡No se la pierdan!', 'http://localhost:8080/eventos/2', '2026-10-24 21:00:00');


-- =====================================================================
--  5. VERIFICACIÓN (opcional)
-- =====================================================================
-- SELECT COUNT(*) AS banners      FROM banner;
-- SELECT COUNT(*) AS eventos      FROM evento;
-- SELECT COUNT(*) AS usuarios     FROM usuario;
-- SELECT COUNT(*) AS multimedia   FROM evento_media;
-- SELECT COUNT(*) AS comentarios  FROM evento_comentario;
-- SELECT COUNT(*) AS valoraciones FROM evento_valoracion;
-- SELECT COUNT(*) AS compartidos  FROM evento_compartido;
