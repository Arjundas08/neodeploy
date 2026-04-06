package com.neodeploy.controller;

import com.neodeploy.model.DeployLog;
import com.neodeploy.service.DeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * DeployController - REST API endpoints for NeoDeploy
 * 
 * WHY THIS EXISTS:
 * This is the "front door" of our application. All HTTP requests come here.
 * Each method handles a different URL endpoint.
 * 
 * ENDPOINTS:
 *   GET  /api/health     → Check if app is running
 *   POST /api/deploy     → Trigger a new deployment
 *   GET  /api/status/{id} → Check status of a deployment
 *   GET  /api/logs       → Get deployment history
 *   GET  /api/dashboard  → Get all dashboard data
 * 
 * @RestController = @Controller + @ResponseBody (returns JSON, not HTML)
 * @RequestMapping sets base URL prefix for all methods
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow frontend to call these APIs
public class DeployController {
    
    private final DeployService deployService;
    
    @Autowired  // Spring automatically injects DeployService here
    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }
    
    /**
     * Health Check Endpoint
     * URL: GET /api/health
     * 
     * Used by:
     * - Jenkins to verify app is running after deploy
     * - Ansible self-heal to check if container is healthy
     * - Dashboard to show green/red status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = deployService.getHealthStatus();
        return ResponseEntity.ok(health);
    }
    
    /**
     * Trigger Deployment
     * URL: POST /api/deploy
     * 
     * Starts a new deployment process. Returns a build ID immediately.
     * The actual deployment happens asynchronously in the background.
     * Use /api/status/{buildId} to track progress.
     */
    @PostMapping("/deploy")
    public ResponseEntity<Map<String, Object>> triggerDeploy(
            @RequestParam(defaultValue = "api") String triggeredBy) {
        
        String buildId = deployService.triggerDeploy(triggeredBy);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("buildId", buildId);
        response.put("message", "Deployment triggered successfully");
        response.put("statusUrl", "/api/status/" + buildId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get Deployment Status
     * URL: GET /api/status/{buildId}
     * 
     * Returns current status of a specific deployment.
     * Status values: PENDING → BUILDING → TESTING → DEPLOYING → SUCCESS/FAILED
     */
    @GetMapping("/status/{buildId}")
    public ResponseEntity<?> getStatus(@PathVariable String buildId) {
        DeployLog log = deployService.getStatus(buildId);
        
        if (log == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Build not found");
            error.put("buildId", buildId);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(log);
    }
    
    /**
     * Get Deployment History
     * URL: GET /api/logs
     * 
     * Returns list of all past deployments, most recent first.
     * Used by dashboard to show deployment history table.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<DeployLog>> getDeployLogs() {
        List<DeployLog> logs = deployService.getDeployHistory();
        return ResponseEntity.ok(logs);
    }
    
    /**
     * Get Dashboard Data
     * URL: GET /api/dashboard
     * 
     * Returns all data needed by the dashboard UI in one call:
     * - Total deploys, success/fail counts
     * - Success rate percentage
     * - Uptime counter
     * - Latest deployment info
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = deployService.getDashboardStats();
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Simple ping endpoint for quick tests
     * URL: GET /api/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("app", "NeoDeploy");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
}
