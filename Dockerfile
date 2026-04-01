# Use Maven image to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use lightweight JDK image to run the app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses PORT environment variable
ENV PORT=8080

EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app/app.jar"]