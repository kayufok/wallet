# Stage 1: Build the application
FROM ubuntu:22.04 AS build

# Install dependencies
RUN apt-get update && apt-get install -y wget unzip openjdk-21-jdk

# Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-7.6-bin.zip
ENV GRADLE_HOME=/opt/gradle/gradle-7.6
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and the build.gradle files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the rest of the project files
COPY . .

# Build the application
RUN ./gradlew build -x test

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
