# ---------- Stage 1: Dependency resolution ----------
FROM maven:3.9.6-eclipse-temurin-17 AS deps
WORKDIR /app
COPY pom.xml pom.xml
RUN mvn dependency:go-offline -B

# ---------- Stage 2: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY . .
RUN mvn clean package -DskipTests

# ---------- Stage 3: Runtime ----------
FROM eclipse-temurin:17-jdk-alpine AS runtime
WORKDIR /app

# Security: Use a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080
EXPOSE 5005

# Security: limit memory, stack size, and CPU usage
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Xss512k", "-Xms256m", "-Xmx512m", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
