#!/usr/bin/env bash
# Compiles and runs the demo (requires JDK 17+).
set -e
rm -rf out
javac -d out $(find src -name '*.java')
java -cp out consulado.Main
