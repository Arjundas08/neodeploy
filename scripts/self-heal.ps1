# ==========================================
# SELF-HEALING MONITOR SCRIPT
# ==========================================

$CONTAINER_NAME = "neodeploy-jenkins"
$DOCKER_IMAGE = "neodeploy:latest"
$APP_PORT = "8086"
$INTERNAL_PORT = "9090"
$CHECK_INTERVAL = 30

$TELEGRAM_BOT_TOKEN = "8758459482:AAGIUKW_w_I7wREihv0OJ9YV04Aqw7PYmGk"
$TELEGRAM_CHAT_ID = "8218103367"
$DOCKER = "C:\Program Files\Docker\Docker\resources\bin\docker.exe"

function Send-TelegramAlert {
    param([string]$Message)
    $url = "https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage"
    try {
        Invoke-RestMethod -Uri $url -Method Post -Body @{chat_id = $TELEGRAM_CHAT_ID; text = $Message} -ErrorAction SilentlyContinue | Out-Null
        Write-Host "[TELEGRAM] Alert sent!" -ForegroundColor Green
    } catch {
        Write-Host "[TELEGRAM] Failed to send" -ForegroundColor Yellow
    }
}

function Test-ContainerHealth {
    $result = & $DOCKER inspect --format='{{.State.Running}}' $CONTAINER_NAME 2>$null
    return $result -eq "true"
}

function Test-AppHealth {
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:$APP_PORT/api/health" -TimeoutSec 5 -ErrorAction Stop
        return $response.status -eq "UP"
    } catch {
        return $false
    }
}

function Restart-Container {
    Write-Host "[ACTION] Restarting container..." -ForegroundColor Yellow
    & $DOCKER stop $CONTAINER_NAME 2>$null
    & $DOCKER rm $CONTAINER_NAME 2>$null
    & $DOCKER run -d --name $CONTAINER_NAME -p "${APP_PORT}:${INTERNAL_PORT}" $DOCKER_IMAGE
    Start-Sleep -Seconds 15
    if (Test-AppHealth) {
        Write-Host "[SUCCESS] Container restarted!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "[FAILED] Container failed to start" -ForegroundColor Red
        return $false
    }
}

# ==========================================
# MAIN LOOP
# ==========================================

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  NEODEPLOY SELF-HEALING MONITOR" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Container: $CONTAINER_NAME"
Write-Host "Port: $APP_PORT"
Write-Host "Interval: ${CHECK_INTERVAL}s"
Write-Host "Press Ctrl+C to stop"
Write-Host ""

Send-TelegramAlert "SELF-HEALING MONITOR STARTED - Watching: $CONTAINER_NAME"

$healCount = 0

while ($true) {
    $ts = Get-Date -Format "HH:mm:ss"
    $containerRunning = Test-ContainerHealth
    $appHealthy = $false
    
    if ($containerRunning) { $appHealthy = Test-AppHealth }
    
    if ($containerRunning -and $appHealthy) {
        Write-Host "[$ts] OK - Container healthy" -ForegroundColor Green
    }
    elseif ($containerRunning -and -not $appHealthy) {
        Write-Host "[$ts] WARNING - App not responding!" -ForegroundColor Yellow
        Write-Host "[$ts] SELF-HEALING TRIGGERED!" -ForegroundColor Magenta
        $success = Restart-Container
        $healCount++
        if ($success) {
            Send-TelegramAlert "SELF-HEALED! Problem: App frozen. Action: Restarted. Status: RECOVERED. Total heals: $healCount"
        } else {
            Send-TelegramAlert "SELF-HEAL FAILED! Problem: App frozen. Manual intervention needed!"
        }
    }
    else {
        Write-Host "[$ts] CRITICAL - Container NOT running!" -ForegroundColor Red
        Write-Host "[$ts] SELF-HEALING TRIGGERED!" -ForegroundColor Magenta
        $success = Restart-Container
        $healCount++
        if ($success) {
            Send-TelegramAlert "SELF-HEALED! Problem: Container crashed. Action: Restarted. Status: RECOVERED. Total heals: $healCount"
        } else {
            Send-TelegramAlert "SELF-HEAL FAILED! Problem: Container crashed. Manual intervention needed!"
        }
    }
    
    Start-Sleep -Seconds $CHECK_INTERVAL
}
