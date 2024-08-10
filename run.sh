#!/bin/sh

if [ -d "classes" ]; then
    rm -rf classes/*
else
    mkdir classes
fi

if [ -f "build/Chess.jar" ]; then
    rm build/Chess.jar
else
    mkdir build
fi

javac -d classes ./src/chess/*.java
jar -cMf ./build/Chess.jar -C classes . -C src/assets/images .
javadoc -d ./src/assets/javadoc -sourcepath ./src -subpackages chess > /dev/null 2>&1
java -cp build/Chess.jar chess/Chess
