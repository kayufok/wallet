# Use a base image that includes OpenJDK 21 and Gradle
FROM gradle:7.6-jdk21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and the build.gradle files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies
RUN ./gradlew build -x test --parallel --continue

# Copy the rest of the project files
COPY . .

# Build the application
RUN ./gradlew build -x test --parallel --continue

# Use a minimal JRE image to run the application
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]