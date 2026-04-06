# NeoDeploy - CI/CD DevOps Pipeline

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java">
  <img src="https://img.shields.io/badge/Docker-✓-blue" alt="Docker">
  <img src="https://img.shields.io/badge/Jenkins-✓-red" alt="Jenkins">
  <img src="https://img.shields.io/badge/Ansible-✓-black" alt="Ansible">
</p>

## 🚀 Overview

**NeoDeploy** is a complete CI/CD pipeline demonstration project showcasing modern DevOps practices:

- **Spring Boot** REST API with health endpoints
- **JUnit 5** test suite with coverage reporting
- **Docker** containerization
- **Jenkins** pipeline automation
- **Ansible** deployment with self-healing
- **Real-time Dashboard** showing deployment status

## 📋 Features

| Feature | Description |
|---------|-------------|
| 🔄 CI/CD Pipeline | Automated build, test, and deploy on every commit |
| 🐳 Docker | Containerized application for consistent deployments |
| 🧪 JUnit Tests | Automated testing with JaCoCo coverage reports |
| 🤖 Ansible | Infrastructure automation with self-healing |
| 📊 Dashboard | Real-time monitoring of deployments |
| ❤️ Health Checks | Automated health monitoring and restart |

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot 3.2.5, Maven
- **Testing:** JUnit 5, Mockito, JaCoCo
- **Containerization:** Docker, Docker Compose
- **CI/CD:** Jenkins Pipeline
- **Automation:** Ansible
- **Database:** H2 (dev), MySQL (prod)
- **Frontend:** HTML5, CSS3, JavaScript (Vanilla)

## 📁 Project Structure

```
neodeploy/
├── src/
│   ├── main/
│   │   ├── java/com/neodeploy/
│   │   │   ├── NeodeployApplication.java    # Main entry point
│   │   │   ├── controller/                   # REST controllers
│   │   │   ├── service/                      # Business logic
│   │   │   └── model/                        # Data models
│   │   └── resources/
│   │       ├── application.properties        # Configuration
│   │       └── static/                       # Dashboard UI
│   └── test/                                 # JUnit tests
├── ansible/
│   ├── playbook.yml                          # Main deployment
│   ├── inventory.ini                         # Server inventory
│   └── self-heal.yml                         # Auto-recovery
├── Dockerfile                                # Container definition
├── docker-compose.yml                        # Multi-container setup
├── Jenkinsfile                               # CI/CD pipeline
└── pom.xml                                   # Maven configuration
```

## 🚦 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check (used by Ansible) |
| POST | `/api/deploy` | Trigger new deployment |
| GET | `/api/status/{buildId}` | Get deployment status |
| GET | `/api/logs` | Get deployment history |
| GET | `/api/dashboard` | Get dashboard statistics |
| GET | `/api/ping` | Simple ping test |

## 🏃 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker Desktop (optional)
- Jenkins (optional)
- WSL2 + Ansible (optional)

### Run Locally (Without Docker)

```bash
# Clone the repository
git clone https://github.com/yourusername/neodeploy.git
cd neodeploy

# Build and run
mvn clean package
java -jar target/neodeploy-0.0.1-SNAPSHOT.jar

# Open browser
# Dashboard: http://localhost:8080/index.html
# Health: http://localhost:8080/api/health
```

### Run with Docker

```bash
# Build and run with Docker Compose
docker-compose up -d

# Check logs
docker-compose logs -f

# Stop
docker-compose down
```

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 📦 Jenkins Pipeline

The Jenkinsfile defines 8 stages:

1. **Checkout** - Clone repository
2. **Build** - `mvn clean package`
3. **Test** - Run JUnit tests
4. **Coverage** - Generate JaCoCo report
5. **Docker Build** - Build container image
6. **Docker Push** - Push to Docker Hub
7. **Deploy** - Run Ansible playbook
8. **Health Check** - Verify application

## 🤖 Self-Healing

Ansible monitors the application and auto-restarts if it fails:

```bash
# Run self-heal check (from WSL2)
cd ansible
ansible-playbook -i inventory.ini self-heal.yml

# Set up cron job for automatic monitoring
crontab -e
# Add: * * * * * cd /path/to/ansible && ansible-playbook -i inventory.ini self-heal.yml
```

## 📊 Dashboard Screenshots

The dashboard shows:
- 🟢 Live application status
- 📈 Total deployments, success rate
- ⏱️ Application uptime
- 📋 Deployment history
- 🔄 Pipeline visualization

## 📐 UML Diagrams

Create these diagrams using [draw.io](https://diagrams.net):

1. **Use Case** - User interactions
2. **Class** - Java classes
3. **Sequence** - Deployment flow
4. **Component** - System components
5. **Deployment** - Infrastructure

## 👤 Author

Your Name - [GitHub](https://github.com/yourusername)

## 📄 License

This project is for educational purposes.

---

Made with ❤️ for DevOps learning

## Demo - 04/06/2026 09:00:09
