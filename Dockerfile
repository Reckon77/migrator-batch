# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/migrator.jar /app/migrator.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/migrator.jar"]

# Expose any necessary ports (optional, depending on your app)
EXPOSE 8080
