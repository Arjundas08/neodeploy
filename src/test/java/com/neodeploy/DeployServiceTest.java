package com.neodeploy;

import com.neodeploy.model.DeployLog;
import com.neodeploy.service.DeployService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * DeployService Tests
 * 
 * WHY WE TEST THE SERVICE:
 * - Service contains the actual business logic
 * - These tests don't need HTTP mocking
 * - Faster to run, easier to debug
 * 
 * Testing philosophy: Test behavior, not implementation.
 * Ask: "What should happen?" not "How is it coded?"
 */
public class DeployServiceTest {
    
    private DeployService deployService;
    
    @BeforeEach
    void setUp() {
        deployService = new DeployService();  // Fresh service for each test
    }
    
    /**
     * TEST 1: triggerDeploy returns a build ID
     */
    @Test
    @DisplayName("triggerDeploy should return non-null build ID")
    void triggerDeploy_ReturnsValidBuildId() {
        // ACT
        String buildId = deployService.triggerDeploy("test");
        
        // ASSERT
        assertNotNull(buildId, "Build ID should not be null");
        assertTrue(buildId.startsWith("BUILD-"), "Build ID should start with BUILD-");
    }
    
    /**
     * TEST 2: Each deploy gets unique ID
     */
    @Test
    @DisplayName("Multiple deploys should get unique IDs")
    void triggerDeploy_GeneratesUniqueIds() throws InterruptedException {
        // ACT
        String buildId1 = deployService.triggerDeploy("test");
        Thread.sleep(5);  // Small delay to ensure different timestamps
        String buildId2 = deployService.triggerDeploy("test");
        
        // ASSERT
        assertNotEquals(buildId1, buildId2, "Each deploy should have unique ID");
    }
    
    /**
     * TEST 3: Health check returns UP
     */
    @Test
    @DisplayName("Health check should return UP status")
    void getHealthStatus_ReturnsUp() {
        // ACT
        Map<String, Object> health = deployService.getHealthStatus();
        
        // ASSERT
        assertEquals("UP", health.get("status"), "Status should be UP");
        assertNotNull(health.get("timestamp"), "Should have timestamp");
        assertNotNull(health.get("uptime"), "Should have uptime");
    }
    
    /**
     * TEST 4: Can get status of deployment
     */
    @Test
    @DisplayName("getStatus should return deploy info")
    void getStatus_ReturnsDeploy() {
        // ARRANGE
        String buildId = deployService.triggerDeploy("junit");
        
        // ACT
        DeployLog log = deployService.getStatus(buildId);
        
        // ASSERT
        assertNotNull(log, "Should find the deployment");
        assertEquals(buildId, log.getBuildId());
        assertEquals("junit", log.getTriggeredBy());
    }
    
    /**
     * TEST 5: Unknown build ID returns null
     */
    @Test
    @DisplayName("getStatus with invalid ID should return null")
    void getStatus_InvalidId_ReturnsNull() {
        // ACT
        DeployLog log = deployService.getStatus("INVALID-BUILD-ID");
        
        // ASSERT
        assertNull(log, "Invalid ID should return null");
    }
    
    /**
     * TEST 6: Dashboard stats work
     */
    @Test
    @DisplayName("Dashboard stats should include required fields")
    void getDashboardStats_ReturnsRequiredFields() {
        // ACT
        Map<String, Object> stats = deployService.getDashboardStats();
        
        // ASSERT
        assertTrue(stats.containsKey("totalDeploys"), "Should have totalDeploys");
        assertTrue(stats.containsKey("successCount"), "Should have successCount");
        assertTrue(stats.containsKey("failedCount"), "Should have failedCount");
        assertTrue(stats.containsKey("successRate"), "Should have successRate");
        assertTrue(stats.containsKey("uptime"), "Should have uptime");
    }
    
    /**
     * TEST 7: Deploy history is tracked
     */
    @Test
    @DisplayName("Deployments should be added to history")
    void triggerDeploy_AddsToHistory() {
        // ARRANGE
        int initialCount = deployService.getDeployHistory().size();
        
        // ACT
        deployService.triggerDeploy("test1");
        deployService.triggerDeploy("test2");
        
        // ASSERT
        assertEquals(initialCount + 2, deployService.getDeployHistory().size());
    }
}
