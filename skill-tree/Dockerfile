FROM openjdk:17-jdk as build
WORKDIR /app
COPY skill-tree/ .
RUN ./mvnw --version
RUN ./mvnw clean install -DskipTests

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/skill-tree/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
