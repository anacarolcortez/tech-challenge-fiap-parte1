# ---------- Stage 1: Dependency resolution ----------
FROM maven:3.9.6-eclipse-temurin-17 AS deps
WORKDIR /app

# Copy only files needed to resolve dependencies
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies and cache them
RUN mvn dependency:go-offline -B

# ---------- Stage 2: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Reuse Maven cache
COPY --from=deps /root/.m2 /root/.m2

# Copy the rest of the source code
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# ---------- Stage 3: Runtime ----------
FROM eclipse-temurin:17-jdk-alpine AS runtime
WORKDIR /app

# Use non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser

# Expose ports
EXPOSE 8080
EXPOSE 5005

# Enable JDWP for remote debugging
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Xss512k", "-Xms256m", "-Xmx512m", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
