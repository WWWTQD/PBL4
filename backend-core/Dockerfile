# PBL4 Backend Dockerfile
# Build: docker build -t pbl4-backend .
# Run: docker run -p 8080:8080 pbl4-backend

FROM maven:3.9-eclipse-temurin-17 as builder

WORKDIR /build

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/documents/health/system || exit 1

# Run application
CMD ["java", "-jar", "app.jar"]

