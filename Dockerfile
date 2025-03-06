## Stage 1: Build the application
#FROM maven:3-eclipse-temurin-19 AS builder
#
#WORKDIR /app
#COPY . .
#
## Build the project (skip tests for faster build)
#RUN mvn clean package -DskipTests
#
## Stage 2: Run the application
#FROM eclipse-temurin:19-jdk
#
#WORKDIR /app
#
## Install JavaFX runtime libraries and Xvfb
#RUN apt-get update && \
#    apt-get install -y openjfx xvfb && \
#    rm -rf /var/lib/apt/lists/*
#
## Copy the built JAR from the builder stage
#COPY --from=builder /app/target/computer-vision-fx-1.0-SNAPSHOT.jar app.jar
#
## Run the application with Xvfb
#CMD ["xvfb-run", "-a", "java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]