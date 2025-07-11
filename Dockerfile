FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY target/bank-cards-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]