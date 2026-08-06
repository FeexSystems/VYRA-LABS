@echo off
title VYRA Development Server
color 0A
echo.
echo ========================================
echo    VYRA AI Agent Platform
echo    Development Server
echo ========================================
echo.

REM Set full paths
set NODE_EXE=C:\Program Files\nodejs\node.exe
set PROJECT_DIR=%~dp0

echo Node.js Version:
"%NODE_EXE%" --version
echo.

echo Project Directory: %PROJECT_DIR%
echo Starting VYRA server...
echo Server will be available at: http://localhost:3000
echo.
echo Press Ctrl+C to stop the server
echo ----------------------------------------
echo.

REM Change to project directory and start server
cd /d "%PROJECT_DIR%"

REM Use tsx from node_modules with full Node.js path
"%NODE_EXE%" node_modules\tsx\dist\cli.mjs server/start.ts

echo.
echo ----------------------------------------
echo Server stopped.
pause