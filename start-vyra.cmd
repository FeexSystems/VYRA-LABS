@echo off
title VYRA Development Server
color 0A
echo.
echo ========================================
echo    VYRA AI Agent Platform
echo    Starting Development Server...
echo ========================================
echo.

REM Set Node.js path
set NODE_PATH=C:\Program Files\nodejs\node.exe

REM Check if Node.js exists
if not exist "%NODE_PATH%" (
    echo ERROR: Node.js not found at %NODE_PATH%
    echo Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)

REM Display Node.js version
echo Checking Node.js installation...
"%NODE_PATH%" --version
if %errorlevel% neq 0 (
    echo ERROR: Node.js is not working properly
    pause
    exit /b 1
)

echo Node.js is working correctly!
echo.

REM Check if package.json exists
if not exist "package.json" (
    echo ERROR: package.json not found. Are you in the correct directory?
    echo Current directory: %CD%
    pause
    exit /b 1
)

REM Install dependencies if node_modules doesn't exist
if not exist "node_modules" (
    echo Installing dependencies...
    "%NODE_PATH%" -e "console.log('Installing npm packages...')"
    call "%NODE_PATH:node.exe=npm.cmd%" install
    if %errorlevel% neq 0 (
        echo ERROR: Failed to install dependencies
        pause
        exit /b 1
    )
    echo Dependencies installed successfully!
    echo.
)

REM Start the development server
echo Starting VYRA development server...
echo Server will be available at: http://localhost:3000
echo Press Ctrl+C to stop the server
echo.

"%NODE_PATH%" dev-server.js

echo.
echo Server stopped.
pause