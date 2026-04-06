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
    
    /**
     * Trigger Jenkins Pipeline Build
     * URL: POST /api/jenkins/build
     * 
     * This calls Jenkins API to start a REAL pipeline build!
     * Handles CSRF crumb and authentication properly.
     */
    @PostMapping("/jenkins/build")
    public ResponseEntity<Map<String, Object>> triggerJenkins() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String jenkinsBaseUrl = "http://localhost:8081";
            String jobName = "neodeploy-pipeline";
            
            // Step 1: Get CSRF crumb from Jenkins
            String crumb = null;
            String crumbField = null;
            
            try {
                java.net.URL crumbUrl = new java.net.URL(jenkinsBaseUrl + "/crumbIssuer/api/json");
                java.net.HttpURLConnection crumbConn = (java.net.HttpURLConnection) crumbUrl.openConnection();
                crumbConn.setRequestMethod("GET");
                crumbConn.setConnectTimeout(5000);
                
                if (crumbConn.getResponseCode() == 200) {
                    java.io.BufferedReader in = new java.io.BufferedReader(
                        new java.io.InputStreamReader(crumbConn.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    
                    // Parse JSON manually (simple approach)
                    String json = content.toString();
                    if (json.contains("crumb") && json.contains("crumbRequestField")) {
                        // Extract crumb value
                        int crumbStart = json.indexOf("\"crumb\":\"") + 9;
                        int crumbEnd = json.indexOf("\"", crumbStart);
                        crumb = json.substring(crumbStart, crumbEnd);
                        
                        // Extract crumbRequestField value
                        int fieldStart = json.indexOf("\"crumbRequestField\":\"") + 21;
                        int fieldEnd = json.indexOf("\"", fieldStart);
                        crumbField = json.substring(fieldStart, fieldEnd);
                    }
                }
                crumbConn.disconnect();
            } catch (Exception e) {
                // Continue without crumb if not available
                System.out.println("Could not get Jenkins crumb: " + e.getMessage());
            }
            
            // Step 2: Trigger the build WITH TOKEN (using GET - works with Jenkins remote trigger)
            String buildToken = "neodeploy-token-2026";
            String buildUrl = jenkinsBaseUrl + "/job/" + jobName + "/build?token=" + buildToken;
            java.net.URL url = new java.net.URL(buildUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");  // Jenkins token-based trigger works with GET
            conn.setConnectTimeout(10000);
            
            // Add CSRF crumb header if we got one
            if (crumb != null && crumbField != null) {
                conn.setRequestProperty(crumbField, crumb);
            }
            
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            if (responseCode == 201 || responseCode == 200) {
                response.put("success", true);
                response.put("message", "✅ Jenkins build triggered successfully!");
                response.put("jenkinsUrl", jenkinsBaseUrl + "/job/" + jobName);
                response.put("status", "BUILD_STARTED");
            } else if (responseCode == 403) {
                // 403 means authentication required
                response.put("success", false);
                response.put("message", "Jenkins requires authentication. Please login to Jenkins first.");
                response.put("jenkinsUrl", jenkinsBaseUrl + "/login");
                response.put("status", "AUTH_REQUIRED");
                response.put("hint", "Login to Jenkins at http://localhost:8081 then try again");
            } else {
                response.put("success", false);
                response.put("message", "Jenkins returned code: " + responseCode);
                response.put("status", "ERROR");
            }
            
        } catch (java.net.ConnectException e) {
            response.put("success", false);
            response.put("message", "Cannot connect to Jenkins. Is it running?");
            response.put("status", "CONNECTION_FAILED");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "ERROR");
        }
        
        return ResponseEntity.ok(response);
    }
}
