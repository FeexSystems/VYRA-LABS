@echo off
REM VYRA Release Keystore Generator
REM This script generates a release keystore for production builds

echo ========================================
echo VYRA Release Keystore Generator
echo ========================================
echo.

REM Check if keytool is available
where keytool >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: keytool not found in PATH
    echo Please ensure Java JDK is installed and keytool is in your PATH
    echo.
    echo You can find keytool in: %JAVA_HOME%\bin
    pause
    exit /b 1
)

echo Generating release keystore...
echo.

REM Prompt for keystore information
set /p KEYSTORE_PASSWORD="Enter keystore password (min 6 chars): "
set /p KEY_ALIAS="Enter key alias (e.g., release): "
set /p KEY_PASSWORD="Enter key password (min 6 chars): "
set /p KEY_DNAME="Enter distinguished name (e.g., CN=VYRA, OU=Development, O=VYRA Labs, L=City, ST=State, C=US): "

REM Generate the keystore
keytool -genkey -v -keystore release.keystore -alias %KEY_ALIAS% -keyalg RSA -keysize 2048 -validity 10000 -storepass %KEYSTORE_PASSWORD% -keypass %KEY_PASSWORD% -dname "%KEY_DNAME%"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo SUCCESS: Release keystore generated!
    echo ========================================
    echo.
    echo Keystore file: release.keystore
    echo Key alias: %KEY_ALIAS%
    echo Validity: 10000 days
    echo.
    echo IMPORTANT: Add these environment variables:
    echo RELEASE_STORE_FILE=release.keystore
    echo RELEASE_STORE_PASSWORD=%KEYSTORE_PASSWORD%
    echo RELEASE_KEY_ALIAS=%KEY_ALIAS%
    echo RELEASE_KEY_PASSWORD=%KEY_PASSWORD%
    echo.
    echo Keep this keystore file secure and backed up!
    echo ========================================
) else (
    echo.
    echo ERROR: Failed to generate keystore
    echo Please check the error messages above
)

echo.
pause
