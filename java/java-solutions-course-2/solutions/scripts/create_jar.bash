#!/bin/bash

DEFAULT_REPO_NAME="java-advanced-2025"
if [ "$#" -eq 0 ]; then
  TEST_REPO="../../$DEFAULT_REPO_NAME"
elif [ "$#" -eq 1 ]; then
  TEST_REPO="../../$1"
else
  echo "Error: too many arguments."
  echo "Usage: ./create_jar.bash <name of the test repository>"
  exit 1
fi

if [ ! -d "$TEST_REPO" ]; then
  echo "Error: test repository not found by path: $TEST_REPO"
  exit 1
fi

CLASS_DIR="classes"
JAR_NAME="Implementor.jar"

# Пути к зависимым JAR-файлам (указываем корректные относительные пути)
# :NOTE: encoding
# :NOTE: correct name
IMPLEMENTOR_JAR="$TEST_REPO/artifacts/info.kgeorgiy.java.advanced.implementor.jar"
TOOLS_JAR="$TEST_REPO/artifacts/info.kgeorgiy.java.advanced.implementor.tools.jar"

if [[ "$OSTYPE" == "msys"* || "$OSTYPE" == "cygwin"* || "$OSTYPE" == "win32" ]]; then
  CPSEP=";"
else
  CPSEP=":"
fi

DEPENDENCY_JARS="${IMPLEMENTOR_JAR}${CPSEP}${TOOLS_JAR}"


SRC_FILES=(../java-solutions/info/kgeorgiy/ja/chuprov/implementor/*.java)

rm -rf "$CLASS_DIR"
mkdir -p "$CLASS_DIR"

echo "Compiling sources..."
javac -d "$CLASS_DIR" -cp "$DEPENDENCY_JARS" "${SRC_FILES[@]}"

echo "Creating manifest..."
MANIFEST_FILE="MANIFEST.MF"
cat > "$MANIFEST_FILE" <<EOF
Manifest-Version: 1.0
Main-Class: info.kgeorgiy.ja.chuprov.implementor.Implementor
Class-Path: $IMPLEMENTOR_JAR $TOOLS_JAR

EOF

echo "Packing into a jar..."
jar cfm "$JAR_NAME" "$MANIFEST_FILE" -C "$CLASS_DIR" .

rm -rf "$CLASS_DIR"

echo "Trying to run jar..."
java -jar "$JAR_NAME"
EXIT_CODE=$?
echo "Return code: $EXIT_CODE"