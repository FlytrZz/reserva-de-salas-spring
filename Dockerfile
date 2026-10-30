FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew bootJar -x test
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/reservaVagas-0.0.1-SNAPSHOT.jar"]
