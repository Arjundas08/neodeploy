@echo off
echo ================================================================
echo   JENKINS TRIGGER SETUP - One-Time Configuration
echo ================================================================
echo.
echo Your Jenkins requires a quick security tweak to allow
echo the dashboard to trigger builds.
echo.
echo OPENING JENKINS NOW...
start http://localhost:8081/manage/configureSecurity/
echo.
echo ================================================================
echo   FOLLOW THESE STEPS IN JENKINS:
echo ================================================================
echo.
echo 1. If asked to login, use your Jenkins credentials
echo.
echo 2. Scroll down to "Authorization" section
echo.
echo 3. Change from "Logged-in users can do anything" to:
echo    "Anyone can do anything"
echo.
echo 4. Click "Save" at the bottom
echo.
echo 5. Come back here and press any key
echo.
echo ================================================================
pause

echo.
echo Testing Jenkins trigger...
curl -X POST http://localhost:8081/job/neodeploy-pipeline/build
echo.
echo.
echo If you see no error, Jenkins is now configured!
echo Go back to http://localhost:9090/index.html and click "Trigger Jenkins"
echo.
pause
