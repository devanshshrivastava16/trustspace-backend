# Multi-stage Dockerfile for building and running the TrustSpace Spring Boot app

# Build stage: use Maven with JDK 17 to build the fat jar
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

# only copy pom first for dependency caching
COPY pom.xml ./
COPY src ./src

# Build the application (skip tests for faster image builds)
RUN mvn -B -DskipTests package --no-transfer-progress

# Runtime stage: small JRE image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /workspace/target/*.jar app.jar

# Listen on default port; Render sets $PORT at runtime
EXPOSE 8080

# Allow additional JVM options via JAVA_OPTS and bind Spring Boot to Render's $PORT
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
