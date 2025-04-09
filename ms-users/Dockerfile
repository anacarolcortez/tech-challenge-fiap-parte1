# ---------- Stage 1: Dependency resolution ----------
FROM maven:3.9.6-eclipse-temurin-17 AS deps
WORKDIR /app

# Copy only the stripped dependency POM to improve caching
COPY pom.xml pom.xml
RUN mvn dependency:go-offline -B

# ---------- Stage 2: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the local Maven repo to avoid redownloading
COPY --from=deps /root/.m2 /root/.m2

# Copy the real full project now
COPY . .

# Build
RUN mvn clean package -DskipTests

# ---------- Stage 3: Runtime ----------
FROM eclipse-temurin:17-jdk-alpine AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
