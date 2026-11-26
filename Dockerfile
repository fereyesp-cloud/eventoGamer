# ==========================================
# Etapa 1: Compilación (Build Stage)
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Descarga de dependencias aprovechando la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:resolve -B

# Copia del código fuente y empaquetado del archivo JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Etapa 2: Entorno de Ejecución (Runtime Stage)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Medida de seguridad (Calidad y Seguridad): no ejecutar la app como root
# y preparar carpeta de archivos multimedia con permisos solo para el usuario de la app
RUN addgroup -S gamergroup && adduser -S gameruser -G gamergroup \
    && mkdir -p /app/uploads \
    && chown -R gameruser:gamergroup /app

# Copiamos únicamente el JAR empaquetado desde la etapa de construcción
COPY --from=builder --chown=gameruser:gamergroup /app/target/*.jar app.jar

USER gameruser

# Exponemos el puerto estándar de Spring Boot
EXPOSE 8080

# Configuración de ejecución con soporte para límites de memoria en contenedores
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
