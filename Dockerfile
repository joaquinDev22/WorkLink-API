# ==========================================
# Stage 1: Build execution with Maven & JDK 21
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy dependency definition and wrapper
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Pre-fetch dependencies to leverage Docker cache
RUN ./mvnw dependency:go-offline -B

# Copy source code and build final executable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Lightweight Runtime Environment
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create unprivileged system user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy generated JAR artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose default Spring Boot HTTP port
EXPOSE 8080

# Environment defaults
ENV SPRING_PROFILES_ACTIVE=prod

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
