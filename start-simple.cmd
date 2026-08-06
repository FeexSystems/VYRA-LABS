@echo off
title VYRA Server - Simple Start
color 0A
echo.
echo ========================================
echo    VYRA - Direct Server Start
echo ========================================
echo.

REM Set paths
set NODE_PATH=C:\Program Files\nodejs\node.exe
set NPX_PATH=C:\Program Files\nodejs\npx.cmd

echo Starting VYRA server directly...
echo Server will be available at: http://localhost:3000
echo.

REM Try to start with tsx directly
if exist "%NPX_PATH%" (
    echo Using npx tsx...
    "%NPX_PATH%" tsx server/start.ts
) else (
    echo npx not found, trying node directly...
    "%NODE_PATH%" server/start.ts
)

echo.
echo Server stopped.
pause