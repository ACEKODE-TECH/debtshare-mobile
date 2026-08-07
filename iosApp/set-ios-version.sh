#!/bin/bash
# Sets MARKETING_VERSION and CURRENT_PROJECT_VERSION from the latest Git
# tag. Looks in both .xcconfig files and project.pbxproj, since different
# Xcode project setups keep these settings in different places.
#
# Usage: ./set-ios-version.sh <path-to-folder-containing-.xcodeproj>
# Run this BEFORE building/archiving (manual or Fastlane) - build settings
# are resolved at the start of a build, so editing them mid-build is too
# late for that same build to pick them up.

set -e

TARGET_DIR="${1:-.}"
cd "$TARGET_DIR"

TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "v0.0.0")
VERSION=${TAG#v}

IFS='.' read -r MAJOR MINOR PATCH <<< "$VERSION"
MAJOR=${MAJOR:-0}
MINOR=${MINOR:-0}
PATCH=${PATCH:-0}
BUILD_NUMBER=$((MAJOR * 10000 + MINOR * 100 + PATCH))

FOUND=0

# .xcconfig files: KEY = value or KEY=value, no semicolon
while IFS= read -r file; do
  if grep -q "MARKETING_VERSION" "$file"; then
    sed -i '' -E "s/MARKETING_VERSION[[:space:]]*=.*/MARKETING_VERSION = $VERSION/" "$file"
    FOUND=1
  fi
  if grep -q "CURRENT_PROJECT_VERSION" "$file"; then
    sed -i '' -E "s/CURRENT_PROJECT_VERSION[[:space:]]*=.*/CURRENT_PROJECT_VERSION = $BUILD_NUMBER/" "$file"
    FOUND=1
  fi
done < <(find . -name "*.xcconfig")

# project.pbxproj: KEY = value; (semicolon-terminated)
while IFS= read -r file; do
  if grep -q "MARKETING_VERSION" "$file"; then
    sed -i '' -E "s/MARKETING_VERSION = [^;]+;/MARKETING_VERSION = $VERSION;/g" "$file"
    FOUND=1
  fi
  if grep -q "CURRENT_PROJECT_VERSION" "$file"; then
    sed -i '' -E "s/CURRENT_PROJECT_VERSION = [^;]+;/CURRENT_PROJECT_VERSION = $BUILD_NUMBER;/g" "$file"
    FOUND=1
  fi
done < <(find . -name "project.pbxproj")

if [ "$FOUND" -eq 0 ]; then
  echo "Warning: could not find MARKETING_VERSION or CURRENT_PROJECT_VERSION anywhere under $TARGET_DIR."
  exit 1
fi

echo "Set iOS version to $VERSION (build $BUILD_NUMBER)"
