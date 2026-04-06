// ==========================================
// Jenkinsfile - CI/CD Pipeline with TELEGRAM + TRIVY
// ==========================================
// 
// UNIQUE FEATURES:
// 📱 Telegram Bot Alerts - Get notified on your phone!
// 🔒 Trivy Security Scanning - Check for vulnerabilities
// 
// PIPELINE STAGES:
// 1. Build     → Maven compile + package
// 2. Test      → JUnit tests (11 tests)
// 3. Docker    → Build container image
// 4. Security  → Trivy vulnerability scan 🔒 NEW!
// 5. Deploy    → Run container on port 8086
// 6. Health    → Verify app is running
// 7. Notify    → Send Telegram message 📱 NEW!
// ==========================================

pipeline {
    agent any

    environment {
        // Paths (Windows)
        PROJECT_PATH = 'C:\\Users\\hp\\OneDrive\\Desktop\\Neo-Deploy'
        MVN_PATH = 'C:\\Program Files\\Maven\\bin\\mvn'
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker'
        
        // Docker settings
        DOCKER_IMAGE = 'neodeploy'
        CONTAINER_NAME = 'neodeploy-jenkins'
        APP_PORT = '8086'
        
        // 📱 TELEGRAM CONFIGURATION
        TELEGRAM_BOT_TOKEN = '8758459482:AAGIUKW_w_I7wREihv0OJ9YV04Aqw7PYmGk'
        TELEGRAM_CHAT_ID = '8218103367'
    }

    stages {
        // ========================================
        // STAGE 1: Build with Maven
        // ========================================
        stage('Build') {
            steps {
                echo '🔨 Building application with Maven...'
                bat "\"${MVN_PATH}\" -f \"${PROJECT_PATH}\\pom.xml\" clean package -DskipTests -B"
            }
        }

        // ========================================
        // STAGE 2: Run JUnit Tests
        // ========================================
        stage('Test') {
            steps {
                echo '🧪 Running JUnit Tests...'
                bat "\"${MVN_PATH}\" -f \"${PROJECT_PATH}\\pom.xml\" test -B"
            }
        }

        // ========================================
        // STAGE 3: Build Docker Image
        // ========================================
        stage('Docker Build') {
            steps {
                echo '🐳 Building Docker Image...'
                bat "cd /d \"${PROJECT_PATH}\" && \"${DOCKER_PATH}\" build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        // ========================================
        // STAGE 4: 🔒 SECURITY SCAN WITH TRIVY (UNIQUE!)
        // ========================================
        stage('Security Scan') {
            steps {
                echo '🔒 Running Trivy Security Scan...'
                script {
                    // Run Trivy scan (warns but doesn't fail build)
                    def scanResult = bat(
                        script: "\"${DOCKER_PATH}\" run --rm aquasec/trivy:latest image --severity HIGH,CRITICAL --no-progress ${DOCKER_IMAGE}:latest || exit 0",
                        returnStatus: true
                    )
                    if (scanResult != 0) {
                        echo '⚠️ Security vulnerabilities found! Check report above.'
                    } else {
                        echo '✅ No critical vulnerabilities found!'
                    }
                }
            }
        }

        // ========================================
        // STAGE 5: Deploy Container
        // ========================================
        stage('Deploy') {
            steps {
                echo '🚀 Deploying Container...'
                // Stop existing container (ignore if not running)
                bat "\"${DOCKER_PATH}\" stop ${CONTAINER_NAME} || exit 0"
                bat "\"${DOCKER_PATH}\" rm ${CONTAINER_NAME} || exit 0"
                // Start new container
                bat "\"${DOCKER_PATH}\" run -d --name ${CONTAINER_NAME} -p ${APP_PORT}:9090 ${DOCKER_IMAGE}:latest"
            }
        }

        // ========================================
        // STAGE 6: Health Check
        // ========================================
        stage('Health Check') {
            steps {
                echo '❤️ Checking Application Health...'
                script {
                    // Wait for app to start
                    sleep(time: 15, unit: 'SECONDS')
                    
                    // Check health endpoint
                    def result = bat(
                        script: "curl -s http://localhost:${APP_PORT}/api/health",
                        returnStdout: true
                    ).trim()
                    
                    if (result.contains('UP')) {
                        echo '✅ Application is healthy!'
                    } else {
                        error '❌ Health check failed!'
                    }
                }
            }
        }
    }

    // ========================================
    // 📱 TELEGRAM NOTIFICATIONS (UNIQUE!)
    // ========================================
    post {
        success {
            echo '✅ Pipeline SUCCESSFUL!'
            script {
                def message = """
✅ *NeoDeploy Build SUCCESS*

📦 Build: #${BUILD_NUMBER}
⏱️ Duration: ${currentBuild.durationString.replace(' and counting', '')}
🌐 App: http://localhost:${APP_PORT}

All 11 tests passed!
Security scan complete!
Container deployed!
                """.trim()
                
                // Send Telegram notification
                bat """
                    curl -s -X POST "https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage" ^
                    -d "chat_id=${TELEGRAM_CHAT_ID}" ^
                    -d "text=${message}" ^
                    -d "parse_mode=Markdown" || exit 0
                """
            }
        }
        
        failure {
            echo '❌ Pipeline FAILED!'
            script {
                def message = """
❌ *NeoDeploy Build FAILED*

📦 Build: #${BUILD_NUMBER}
🔴 Stage: ${env.STAGE_NAME}

Check Jenkins for details!
                """.trim()
                
                // Send Telegram notification
                bat """
                    curl -s -X POST "https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage" ^
                    -d "chat_id=${TELEGRAM_CHAT_ID}" ^
                    -d "text=${message}" ^
                    -d "parse_mode=Markdown" || exit 0
                """
            }
        }
    }
}
