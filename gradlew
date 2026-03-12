#!/usr/bin/env bash
set -euo pipefail

GRADLE_VERSION=8.7
GRADLE_BASE_DIR="$(pwd)/.gradle/custom"
GRADLE_DIR="${GRADLE_BASE_DIR}/gradle-${GRADLE_VERSION}"

if [ ! -x "${GRADLE_DIR}/bin/gradle" ]; then
  mkdir -p "${GRADLE_BASE_DIR}"
  echo "Downloading Gradle ${GRADLE_VERSION}..."
  curl -sL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o "${GRADLE_BASE_DIR}/gradle.zip"
  unzip -q "${GRADLE_BASE_DIR}/gradle.zip" -d "${GRADLE_BASE_DIR}"
  mv "${GRADLE_BASE_DIR}/gradle-${GRADLE_VERSION}" "${GRADLE_DIR}" || true
fi

exec "${GRADLE_DIR}/bin/gradle" "$@"

