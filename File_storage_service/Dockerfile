FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar


ENTRYPOINT ["java", "-Dserver.address=0.0.0.0", "-jar", "app.jar"]
