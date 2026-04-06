package com.neodeploy.service;

import com.neodeploy.model.DeployLog;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DeployService - Core business logic for deployments
 * 
 * WHY THIS EXISTS:
 * Controllers should be thin - they just handle HTTP.
 * All the real work happens here in the Service layer.
 * This makes testing easier (we can test service without HTTP).
 * 
 * @Service tells Spring this is a service component (auto-detected)
 */
@Service
public class DeployService {
    
    // In-memory storage (in real app, this would be a JPA Repository)
    private final Map<String, DeployLog> deployments = new ConcurrentHashMap<>();
    private final List<DeployLog> deployHistory = Collections.synchronizedList(new ArrayList<>());
    private LocalDateTime appStartTime = LocalDateTime.now();
    
    /**
     * Trigger a new deployment
     * Returns a unique build ID that can be used to track status
     */
    public String triggerDeploy(String triggeredBy) {
        String buildId = "BUILD-" + System.currentTimeMillis();
        
        DeployLog log = new DeployLog(buildId, "PENDING");
        log.setTriggeredBy(triggeredBy);
        log.setMessage("Deployment initiated");
        
        deployments.put(buildId, log);
        deployHistory.add(log);
        
        // Simulate async deployment process
        simulateDeployment(buildId);
        
        return buildId;
    }
    
    /**
     * Get status of a specific deployment
     */
    public DeployLog getStatus(String buildId) {
        return deployments.get(buildId);
    }
    
    /**
     * Get current application health status
     */
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("uptime", calculateUptime());
        health.put("totalDeploys", deployHistory.size());
        health.put("successfulDeploys", countByStatus("SUCCESS"));
        health.put("failedDeploys", countByStatus("FAILED"));
        health.put("version", "1.0.0");
        return health;
    }
    
    /**
     * Get deployment history (most recent first)
     */
    public List<DeployLog> getDeployHistory() {
        List<DeployLog> sorted = new ArrayList<>(deployHistory);
        sorted.sort((a, b) -> b.getStartTime().compareTo(a.getStartTime()));
        return sorted;
    }
    
    /**
     * Get latest deployment
     */
    public DeployLog getLatestDeploy() {
        if (deployHistory.isEmpty()) {
            return null;
        }
        return deployHistory.get(deployHistory.size() - 1);
    }
    
    /**
     * Get dashboard statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDeploys", deployHistory.size());
        stats.put("successCount", countByStatus("SUCCESS"));
        stats.put("failedCount", countByStatus("FAILED"));
        stats.put("pendingCount", countByStatus("PENDING") + countByStatus("BUILDING") + countByStatus("DEPLOYING"));
        stats.put("successRate", calculateSuccessRate());
        stats.put("lastDeploy", getLatestDeploy());
        stats.put("uptime", calculateUptime());
        stats.put("appStartTime", appStartTime.toString());
        return stats;
    }
    
    // ===== Private Helper Methods =====
    
    private void simulateDeployment(String buildId) {
        // Run deployment simulation in background thread
        new Thread(() -> {
            try {
                DeployLog log = deployments.get(buildId);
                
                // Stage 1: Building
                log.setStatus("BUILDING");
                log.setMessage("Compiling source code...");
                Thread.sleep(2000);
                
                // Stage 2: Testing
                log.setStatus("TESTING");
                log.setMessage("Running JUnit tests...");
                Thread.sleep(2000);
                
                // Stage 3: Deploying
                log.setStatus("DEPLOYING");
                log.setMessage("Deploying to container...");
                Thread.sleep(2000);
                
                // Stage 4: Complete (90% success rate for demo)
                if (Math.random() > 0.1) {
                    log.setStatus("SUCCESS");
                    log.setMessage("Deployment completed successfully!");
                    log.setDockerImage("neodeploy:latest");
                } else {
                    log.setStatus("FAILED");
                    log.setMessage("Deployment failed: Health check timeout");
                }
                log.setEndTime(LocalDateTime.now());
                
            } catch (InterruptedException e) {
                DeployLog log = deployments.get(buildId);
                log.setStatus("FAILED");
                log.setMessage("Deployment interrupted: " + e.getMessage());
                log.setEndTime(LocalDateTime.now());
            }
        }).start();
    }
    
    private long countByStatus(String status) {
        return deployHistory.stream()
                .filter(d -> status.equals(d.getStatus()))
                .count();
    }
    
    private String calculateUptime() {
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(appStartTime, now).getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    
    private double calculateSuccessRate() {
        if (deployHistory.isEmpty()) return 100.0;
        long success = countByStatus("SUCCESS");
        long total = countByStatus("SUCCESS") + countByStatus("FAILED");
        if (total == 0) return 100.0;
        return Math.round((double) success / total * 1000) / 10.0;
    }
}
