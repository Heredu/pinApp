# Etapa de construcción con JDK 21
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa de producción con JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia el JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno para optimización
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS1} -jar app.jar"]