# Use a minimal OpenJDK 17 base image
FROM eclipse-temurin:17-jdk-alpine as base

# Set working directory
WORKDIR /app

# Add a non-root user for security (optional but recommended)
RUN adduser -D -u 1000 appuser

# Copy the JAR file
COPY target/uno-risk-service.jar app.jar

#Expose the port
EXPOSE 8080

# Set ownership to the app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser


# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
