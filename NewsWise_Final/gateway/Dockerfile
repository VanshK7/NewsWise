#Use OpenJDK base image
FROM openjdk:23-jdk-slim

#set working directory
WORKDIR /app

COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]


