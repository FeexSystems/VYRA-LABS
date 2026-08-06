@echo off
title VYRA - Direct Server Start
color 0A
echo.
echo ========================================
echo    VYRA AI Agent Platform
echo    Direct Server Startup
echo ========================================
echo.

REM Set Node.js path
set NODE_EXE=C:\Program Files\nodejs\node.exe
set NPX_CMD=C:\Program Files\nodejs\npx.cmd

echo Checking Node.js installation...
"%NODE_EXE%" --version
if %errorlevel% neq 0 (
    echo ERROR: Node.js not working
    pause
    exit /b 1
)

echo.
echo Starting VYRA server directly with tsx...
echo Server will be available at: http://localhost:3000
echo Press Ctrl+C to stop the server
echo.

REM Start server directly with tsx
if exist "%NPX_CMD%" (
    echo Using npx tsx to start server...
    "%NPX_CMD%" tsx server/start.ts
) else (
    echo npx not found, trying alternative method...
    "%NODE_EXE%" --loader tsx/esm server/start.ts
)

echo.
echo Server stopped.
pause