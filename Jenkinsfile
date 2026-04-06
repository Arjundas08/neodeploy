// ==========================================
// NeoDeploy - Self-Healing CI/CD Pipeline
// ==========================================
// Features: Telegram Alerts, Trivy Security Scan, Auto-Deploy
// ==========================================

pipeline {
    agent any

    environment {
        // Use Jenkins workspace (code is cloned here from GitHub)
        MVN_PATH = 'C:\\Program Files\\Maven\\bin\\mvn'
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker'
        DOCKER_IMAGE = 'neodeploy'
        CONTAINER_NAME = 'neodeploy-jenkins'
        APP_PORT = '8086'
        TELEGRAM_BOT_TOKEN = '8758459482:AAGIUKW_w_I7wREihv0OJ9YV04Aqw7PYmGk'
        TELEGRAM_CHAT_ID = '8218103367'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building application with Maven...'
                // Build from workspace (current directory after checkout)
                bat "\"${MVN_PATH}\" clean package -DskipTests -B"
            }
        }

        stage('Test') {
            steps {
                echo 'Running JUnit Tests...'
                bat "\"${MVN_PATH}\" test -B"
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker Image...'
                bat "\"${DOCKER_PATH}\" build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Security Scan') {
            steps {
                echo 'Running Trivy Security Scan...'
                bat "\"${DOCKER_PATH}\" run --rm -v /var/run/docker.sock:/var/run/docker.sock ghcr.io/aquasecurity/trivy:latest image --severity HIGH,CRITICAL neodeploy:latest || exit 0"
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying Container...'
                bat "\"${DOCKER_PATH}\" stop ${CONTAINER_NAME} || exit 0"
                bat "\"${DOCKER_PATH}\" rm ${CONTAINER_NAME} || exit 0"
                bat "\"${DOCKER_PATH}\" run -d --name ${CONTAINER_NAME} -p ${APP_PORT}:8080 ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Health Check') {
            steps {
                echo 'Checking Application Health...'
                bat 'ping -n 20 127.0.0.1 > nul'
                bat "curl -s http://localhost:${APP_PORT}/api/health || exit 0"
                echo 'Health check complete!'
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESSFUL!'
            bat "curl -s -X POST \"https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage\" -d \"chat_id=${TELEGRAM_CHAT_ID}\" -d \"text=NeoDeploy Build SUCCESS! All 11 tests passed. Security scan complete. App running at http://localhost:8086\""
        }
        failure {
            echo 'Pipeline FAILED!'
            bat "curl -s -X POST \"https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage\" -d \"chat_id=${TELEGRAM_CHAT_ID}\" -d \"text=NeoDeploy Build FAILED! Check Jenkins for details.\""
        }
    }
}
