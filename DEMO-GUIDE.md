# 🎯 NeoDeploy - Complete Localhost Demo Guide

## 🚀 Quick Demo Start (5 Minutes)

### Step 1: Start the Application
```bash
# In your project directory
mvn spring-boot:run
```
**Wait for**: `Started NeodeployApplication in X.XXX seconds`

### Step 2: Open the Dashboard
Open your browser to: **http://localhost:8080/index.html**

### Step 3: Test the Features
1. Click "Quick Deploy" button
2. Watch the deployment progress animation
3. See the deployment history update
4. Check the statistics cards update

---

## 🎬 Complete Demo Flow for Teacher

### Part 1: Show the Dashboard (2 min)
1. Open: `http://localhost:8080/index.html`
2. Point out:
   - ✅ Live status indicator (green = healthy)
   - 📊 Statistics cards (deploys, success rate, uptime)
   - 🔄 Pipeline visualization diagram
   - 📋 Deployment history table

### Part 2: Trigger a Deployment (3 min)
1. Click **"Quick Deploy"** button
2. Show the animated progress bar:
   - Pending → Building → Testing → Deploying → Done
3. Watch the stats update in real-time
4. Show the new entry in deployment history

### Part 3: API Endpoints Demo (2 min)
Open in browser or Postman:

**Health Check:**
```
http://localhost:8080/api/health
```
Response: `{"status":"UP","timestamp":"..."}`

**Dashboard Stats:**
```
http://localhost:8080/api/dashboard
```
Shows: Total deploys, success rate, uptime

**Deployment History:**
```
http://localhost:8080/api/logs
```
Shows: All past deployments

**Ping Test:**
```
http://localhost:8080/api/ping
```
Response: `{"message":"pong","timestamp":"..."}`

### Part 4: Show the Code (3 min)

**Main Application:**
```
src/main/java/com/neodeploy/NeodeployApplication.java
```

**REST Controller:**
```
src/main/java/com/neodeploy/controller/DeployController.java
```

**Tests:**
```
src/test/java/com/neodeploy/
```

### Part 5: Run Tests (2 min)
```bash
mvn test
```
Show: 11 tests passing ✅

**Generate Coverage Report:**
```bash
mvn clean test jacoco:report
```
Open: `target/site/jacoco/index.html`
Show: Code coverage percentage

### Part 6: Docker Demo (Optional - 2 min)
```bash
# Build Docker image
docker build -t neodeploy:latest .

# Run container
docker run -p 8080:8080 neodeploy:latest
```
Show: App running in container

### Part 7: Jenkins & Ansible (Explain - 2 min)

**Jenkinsfile** - Show the pipeline stages:
1. Checkout code
2. Maven build
3. Run tests
4. JaCoCo coverage
5. Docker build
6. Ansible deploy
7. Health check

**Ansible** - Show deployment automation:
```
ansible/playbook.yml - Deploy application
ansible/self-heal.yml - Auto-restart if down
```

---

## 📝 Key Points to Mention

### ✨ Technical Highlights:
- **Spring Boot 3.2.5** with Java 17
- **REST API** with 6 endpoints
- **H2 in-memory database** (no setup needed)
- **JUnit 5** with 11 test cases
- **JaCoCo** code coverage reporting
- **Docker** containerization
- **Jenkins** CI/CD pipeline (6 stages)
- **Ansible** for deployment automation
- **Real-time dashboard** with live updates

### 🎯 DevOps Features:
- ✅ Continuous Integration (automated builds)
- ✅ Continuous Deployment (automated releases)
- ✅ Self-healing (auto-restart on failure)
- ✅ Health monitoring
- ✅ Security scanning (Trivy)
- ✅ Test automation
- ✅ Containerization

---

## 🔥 Demo Script (Read Out Loud)

"Hello Sir/Ma'am, 

I'll demonstrate my CI/CD DevOps Pipeline project called **NeoDeploy**.

**[Show Dashboard]**
This is the real-time dashboard showing deployment statistics and pipeline status. The application is currently running and healthy as you can see from the green indicator.

**[Click Quick Deploy]**
Let me trigger a deployment. Watch as it goes through multiple stages: pending, building, testing, and deploying. The progress bar shows real-time status.

**[Show APIs]**
Here are the REST endpoints. The health check returns application status, which is used by our monitoring system.

**[Show Code]**
The backend is built with Spring Boot. Here's the main controller handling deployments, and here are our JUnit tests.

**[Run Tests]**
Running the test suite now... All 11 tests passed successfully. We also generate code coverage reports using JaCoCo.

**[Show Jenkins Pipeline]**
This Jenkinsfile defines our complete CI/CD pipeline - from code checkout to automated testing, Docker image building, and deployment using Ansible.

**[Show Docker]**
The application is fully containerized, making it easy to deploy anywhere consistently.

**[Explain Self-Healing]**
Finally, we have an Ansible playbook that monitors the application and automatically restarts it if it goes down - that's our self-healing capability.

That's the complete demonstration. Thank you!"

---

## ⚡ Quick Troubleshooting

### Port 8080 already in use?
```bash
# Find process
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F
```

### Maven build fails?
```bash
# Clean and rebuild
mvn clean install -DskipTests
```

### Dashboard not loading?
1. Check app is running: `http://localhost:8080/api/health`
2. Clear browser cache
3. Try: `http://localhost:8080/index.html`

---

## 📸 Screenshot Checklist

Take these screenshots to include in report:
- [ ] Dashboard main view
- [ ] Deployment in progress
- [ ] Deployment history table
- [ ] API health check response
- [ ] Test results (mvn test)
- [ ] JaCoCo coverage report
- [ ] Jenkins pipeline (if available)
- [ ] Docker container running

---

**Good luck with your demo! 🚀**
