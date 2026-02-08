@echo off
setlocal

set CLASS_DIR=classes
set BUILD_DIR=build

REM Clean and create classes directory
if exist "%CLASS_DIR%" (
    rmdir /s /q "%CLASS_DIR%"
)
mkdir "%CLASS_DIR%"

REM Clean and create build directory
if exist "%BUILD_DIR%" (
    rmdir /s /q "%BUILD_DIR%"
)
mkdir "%BUILD_DIR%"

REM Compile
javac -d "%CLASS_DIR%" src\chess\*.java

REM Pack JAR
jar -cMf "%BUILD_DIR%\Chess.jar" -C "%CLASS_DIR%" . -C src\assets\images .

REM Generate javadoc (suppress output)
javadoc -d src\assets\javadoc -sourcepath src -subpackages chess >nul 2>&1

REM Run
java -cp "%BUILD_DIR%\Chess.jar" chess.Chess

endlocal
