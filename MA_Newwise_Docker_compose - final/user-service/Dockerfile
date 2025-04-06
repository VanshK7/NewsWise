#Use OpenJDK base image
FROM openjdk:23-jdk-slim

#set working directory
WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
