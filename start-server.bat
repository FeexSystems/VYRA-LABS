@echo off
echo Starting VYRA Development Server...
echo.

REM Try different Node.js paths
if exist "C:\Program Files\nodejs\node.exe" (
    echo Found Node.js at C:\Program Files\nodejs\
    "C:\Program Files\nodejs\node.exe" dev-server.js
    goto :end
)

if exist "C:\Program Files (x86)\nodejs\node.exe" (
    echo Found Node.js at C:\Program Files (x86)\nodejs\
    "C:\Program Files (x86)\nodejs\node.exe" dev-server.js
    goto :end
)

REM Try using npx if available
npx tsx server/start.ts 2>nul
if %errorlevel% equ 0 goto :end

REM Try using node directly
node dev-server.js 2>nul
if %errorlevel% equ 0 goto :end

echo.
echo ERROR: Node.js not found or not properly installed.
echo.
echo Please install Node.js from https://nodejs.org/
echo Make sure to:
echo 1. Download the LTS version
echo 2. Run the installer as Administrator
echo 3. Restart your command prompt after installation
echo 4. Verify installation with: node --version
echo.
pause

:end
echo.
echo Server stopped.
pause