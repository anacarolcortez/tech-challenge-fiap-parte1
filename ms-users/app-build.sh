#!/bin/bash

set -e

BASE_IMAGE_NAME="tech-challenge-fiap-01"
INCREMENT_LEVEL="${1:-patch}"  # default to 'patch'

# Get the latest semantic version (e.g., 0.0.1)
LATEST_VERSION=$(docker images --format "{{.Repository}}:{{.Tag}}" | \
  grep "^$BASE_IMAGE_NAME:" | \
  awk -F: '{print $2}' | \
  grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' | \
  sort -t. -k1,1n -k2,2n -k3,3n | \
  tail -n 1)

# If no version exists, start from 0.0.0
if [ -z "$LATEST_VERSION" ]; then
  LATEST_VERSION="0.0.0"
fi

IFS='.' read -r MAJOR MINOR PATCH <<< "$LATEST_VERSION"

# Increment the right version part
case "$INCREMENT_LEVEL" in
  major)
    MAJOR=$((MAJOR + 1)); MINOR=0; PATCH=0 ;;
  minor)
    MINOR=$((MINOR + 1)); PATCH=0 ;;
  patch)
    PATCH=$((PATCH + 1)) ;;
  *)
    echo "❌ Invalid increment level: $INCREMENT_LEVEL"
    echo "   Use: patch (default), minor, or major"
    exit 1
    ;;
esac

NEW_VERSION="$MAJOR.$MINOR.$PATCH"
VERSION_TAG="$BASE_IMAGE_NAME:$NEW_VERSION"
LATEST_TAG="$BASE_IMAGE_NAME:latest"

echo "🔍 Last version found: $LATEST_VERSION"
echo "🚀 Building new version: $VERSION_TAG"
echo "📦 Also tagging as: $LATEST_TAG"

# Build the image with both tags
docker build -t "$VERSION_TAG" -t "$LATEST_TAG" .

if [ $? -eq 0 ]; then
  echo "✅ Successfully built:"
  echo "   - $VERSION_TAG"
  echo "   - $LATEST_TAG"
else
  echo "❌ Docker build failed"
  exit 1
fi
