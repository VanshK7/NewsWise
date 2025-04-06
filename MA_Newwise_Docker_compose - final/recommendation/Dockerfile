#Use OpenJDK base image
FROM openjdk:23-jdk-slim

#set working directory
WORKDIR /app

COPY target/recommendation-0.0.1-SNAPSHOT.jar /app/reccomendation-service.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/reccomendation-service.jar"]

