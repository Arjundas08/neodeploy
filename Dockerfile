# ==========================================
# Dockerfile for NeoDeploy
# ==========================================
# 
# WHAT IS A DOCKERFILE?
# A recipe that tells Docker how to build your application image.
# Think of it like cooking instructions - Docker follows these
# steps to create a container that can run anywhere.
#
# HOW TO USE:
#   docker build -t neodeploy:latest .
#   docker run -p 8080:8080 neodeploy:latest
# ==========================================

# Stage 1: Build stage (optional - for multi-stage builds)
# We use pre-built JAR from Maven, so we skip build stage

# Base image: Eclipse Temurin JRE 17 on Alpine Linux
# WHY THIS?
# - Temurin is the official OpenJDK replacement
# - Alpine is tiny (~5MB) - makes our image small
# - JRE (not JDK) - we only need to RUN Java, not compile
FROM eclipse-temurin:17-jre-alpine

# Add metadata labels
LABEL maintainer="NeoDeploy Team"
LABEL version="1.0.0"
LABEL description="CI/CD DevOps Pipeline Demo Application"

# Set working directory inside container
WORKDIR /app

# Create a non-root user for security
# WHY? Running as root in containers is a security risk
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the JAR file from Maven build
# The JAR is created by: mvn clean package
COPY target/neodeploy-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

# Expose port 8080 (documentation - actual port mapping done at runtime)
EXPOSE 8080

# Health check - Docker will monitor if app is healthy
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/api/health || exit 1

# Environment variables (can be overridden at runtime)
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE=default

# Run the application
# exec form (preferred) - signals properly forwarded to Java process
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
