@echo off
echo ========================================
echo   NeoDeploy - Opening Demo Dashboard
echo ========================================
echo.
echo Starting browser tabs...
echo.

REM Open Dashboard
start http://localhost:9090/index.html
timeout /t 2 /nobreak >nul

REM Open Health Check
start http://localhost:9090/api/health
timeout /t 1 /nobreak >nul

REM Open Ping Test
start http://localhost:9090/api/ping

echo.
echo ========================================
echo   Demo URLs Opened!
echo ========================================
echo.
echo Main Dashboard:  http://localhost:9090/index.html
echo Health Check:    http://localhost:9090/api/health  
echo Ping Test:       http://localhost:9090/api/ping
echo Dashboard API:   http://localhost:9090/api/dashboard
echo.
echo Click "Quick Deploy" button to start a deployment!
echo.
pause
