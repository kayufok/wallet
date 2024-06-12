# Stage 1: Build the application
FROM gradle:jdk21 AS build

# Set the working directory in the container
WORKDIR /home/gradle/src

# Copy the Gradle wrapper and the build.gradle files
COPY --chown=gradle:gradle . /home/gradle/src

# Grant execution permissions for the Gradle wrapper
RUN chmod +x gradlew

# Copy the rest of the project files
COPY . .

# Build the application
RUN gradle build --no-daemon

# Stage 2: Run the application
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/src/build/libs /app/

# Copy the built JAR file from the build stage to the correct directory
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]