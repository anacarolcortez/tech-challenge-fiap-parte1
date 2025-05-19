#!/bin/bash

set -e

APP_BUILD_SCRIPT="./app-build.sh"
INCREMENT_LEVEL="${1:-patch}"
SKIP_TESTS=false
FORCE_NO_CACHE=false
M2_REPO="$HOME/.m2/repository"

# Handle second or third argument as options
for arg in "$@"; do
  case $arg in
    --skip-tests)
      SKIP_TESTS=true
      ;;
    --no-cache)
      FORCE_NO_CACHE=true
      ;;
  esac
done

# === Functions ===

print_help() {
  echo "📘 Usage: ./local-continuous-integration.sh [patch|minor|major] [--skip-tests] [--no-cache]"
  echo "Runs a full local CI: stops services, builds image, optionally runs Maven tests, and restarts services."
  echo ""
  echo "Arguments:"
  echo "  patch|minor|major   Version increment level (default: patch)"
  echo "  --skip-tests         Skips JUnit Maven tests"
  echo "  --no-cache           Does not mount local Maven repository (~/.m2) to avoid stale dependency issues"
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

  DOCKER_ARGS=(
    -v "$(pwd)":/app
    -w /app
    --rm
  )

  if [ "$FORCE_NO_CACHE" = false ]; then
    DOCKER_ARGS+=("-v" "$M2_REPO":/root/.m2/repository)
    echo "📦 Using local Maven cache at $M2_REPO"
  else
    echo "🚫 Skipping local Maven cache (forcing fresh dependencies)"
  fi

  docker run "${DOCKER_ARGS[@]}" \
    maven:3.9.6-eclipse-temurin-17 mvn clean test

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

if [ "$SKIP_TESTS" = false ]; then
  run_maven_tests
else
  echo "⏭️  Skipping Maven tests as requested."
fi

echo "🚀 Starting Docker Compose services..."
docker compose up -d

echo "🧹 Cleaning up dangling Docker images..."
docker image prune -f

echo ""
echo "🎉 Local CI finished successfully!"
if [ "$SKIP_TESTS" = true ]; then
  echo "📦 Your app is built (without tests), and running."
else
  echo "📦 Your app is built, tested and running."
fi

echo "📜 View logs: docker compose logs -f"
