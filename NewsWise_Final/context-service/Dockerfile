#Use OpenJDK base image
FROM openjdk:23-jdk-slim

#set working directory
WORKDIR /app

COPY target/context-service-0.0.1-SNAPSHOT.jar /app/context-service.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app/context-service.jar"]


