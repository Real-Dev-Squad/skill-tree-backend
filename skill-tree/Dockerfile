FROM openjdk:17-jdk as build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
