FROM maven:3.8.7-eclipse-temurin-17 AS build

COPY pom.xml /app/
COPY src /app/src

WORKDIR /app

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/nu-pix-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]