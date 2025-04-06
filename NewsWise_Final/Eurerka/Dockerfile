# Use OpenJDK base image
FROM openjdk:23-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/Eurerka-0.0.1-SNAPSHOT.jar /app/eureka-server.jar

# Expose the Eureka Server port
EXPOSE 8761

# Start the Eureka Server
ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]
