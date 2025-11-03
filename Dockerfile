# ==== Stage 1: Build the application ====
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ==== Stage 2: Run the application ====
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8007

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
