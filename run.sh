#!/bin/sh

CLASS_DIR="classes"
BUILD_DIR="build"

[ -d "$CLASS_DIR" ] && rm -rf "$CLASS_DIR"
mkdir "$CLASS_DIR"

[ -d "$BUILD_DIR" ] && rm -rf "$BUILD_DIR"
mkdir "$BUILD_DIR"

# Compile
javac -d "$CLASS_DIR" src/chess/*.java
# Pack JAR
jar -cMf "${BUILD_DIR}/Chess.jar" -C "$CLASS_DIR" . -C src/assets/images .
# Generate javadoc
javadoc -d src/assets/javadoc -sourcepath src -subpackages chess > /dev/null 2>&1
# Run
java -cp "${BUILD_DIR}/Chess.jar" chess/Chess
