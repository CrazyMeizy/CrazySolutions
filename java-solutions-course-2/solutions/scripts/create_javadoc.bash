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

javadoc -d ../javadoc \
  ../java-solutions/info/kgeorgiy/ja/chuprov/implementor/CompilationException.java \
  ../java-solutions/info/kgeorgiy/ja/chuprov/implementor/DefaultValue.java \
  ../java-solutions/info/kgeorgiy/ja/chuprov/implementor/InterfaceKeyword.java \
  ../java-solutions/info/kgeorgiy/ja/chuprov/implementor/Implementor.java \
  "$TEST_REPO"/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java \
  "$TEST_REPO"/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ImplerException.java \
  "$TEST_REPO"/modules/info.kgeorgiy.java.advanced.implementor.tools/info/kgeorgiy/java/advanced/implementor/tools/JarImpler.java \
  -private \
  -author \
  -link https://docs.oracle.com/en/java/javase/21/docs/api/
