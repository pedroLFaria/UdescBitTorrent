FROM maven:3.8.4-openjdk-17-slim AS builder
COPY . /app
WORKDIR /app
RUN mvn clean package
FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder /app/target/my-app-1.0-SNAPSHOT.jar /app/my-app.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "my-app.jar"]
