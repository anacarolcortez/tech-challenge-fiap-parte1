#!/bin/bash

set -e

APP_BUILD_SCRIPT="./app-build.sh"
INCREMENT_LEVEL="${1:-patch}"
M2_REPO="$HOME/.m2/repository"  # Local Maven repository on your host system

# === Functions ===

print_help() {
  echo "📘 Usage: ./local-continuous-integration.sh [patch|minor|major]"
  echo "Runs a full local CI: stops services, builds image, runs Maven tests, restarts services."
}

validate_increment_level() {
  if [[ "$INCREMENT_LEVEL" != "patch" && "$INCREMENT_LEVEL" != "minor" && "$INCREMENT_LEVEL" != "major" ]]; then
    echo "❌ Invalid version increment level: $INCREMENT_LEVEL"
    echo "   Must be one of: patch, minor, major"
    exit 1
  fi
}

run_maven_tests() {
  echo "🧪 Running Maven tests using Docker with Maven 3.9.6 and Eclipse Temurin 17..."

  # Create a persistent volume for Maven's local repository
  docker run --rm \
    -v "$(pwd)":/app \
    -v "$M2_REPO":/root/.m2/repository \
    -w /app \
    maven:3.9.6-eclipse-temurin-17 mvn test

  if [ $? -eq 0 ]; then
    echo "✅ Maven tests passed."
  else
    echo "❌ Maven tests failed. Stopping CI."
    exit 1
  fi
}

# === Start Script ===

if [[ "$INCREMENT_LEVEL" == "--help" || "$INCREMENT_LEVEL" == "-h" ]]; then
  print_help
  exit 0
fi

validate_increment_level

if [ ! -f "$APP_BUILD_SCRIPT" ]; then
  echo "❌ Error: $APP_BUILD_SCRIPT not found!"
  exit 1
fi

echo "🔍 Validating docker-compose.yml..."
docker compose config > /dev/null || { echo "❌ Invalid docker-compose.yml"; exit 1; }

echo "🛑 Stopping Docker Compose services..."
docker compose down

echo "🔨 Building Docker image with version increment: $INCREMENT_LEVEL"
"$APP_BUILD_SCRIPT" "$INCREMENT_LEVEL"

run_maven_tests

echo "🚀 Starting Docker Compose services..."
docker compose up -d

echo "🧹 Cleaning up dangling Docker images..."
docker image prune -f

echo ""
echo "🎉 Local CI finished successfully!"
echo "📦 Your app is built, tested, and running."
echo "📜 View logs: docker compose logs -f"
