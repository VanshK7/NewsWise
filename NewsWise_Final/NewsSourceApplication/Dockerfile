#Use OpenJDK base image
FROM openjdk:23-jdk-slim

#set working directory
WORKDIR /app

COPY target/NewsSourceApplication-0.0.1-SNAPSHOT.jar /app/news-service.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/app/news-service.jar"]


