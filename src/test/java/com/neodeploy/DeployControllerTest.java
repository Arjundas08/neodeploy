package com.neodeploy;

import com.neodeploy.controller.DeployController;
import com.neodeploy.service.DeployService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DeployController Tests
 * 
 * WHY WE TEST:
 * - Ensures our REST APIs work correctly
 * - Jenkins runs these tests automatically
 * - If ANY test fails, deployment is blocked
 * - This is how professional teams prevent bugs in production
 * 
 * @WebMvcTest - Tests only the web layer (fast, no database)
 */
@WebMvcTest(DeployController.class)
public class DeployControllerTest {
    
    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests
    
    @MockBean
    private DeployService deployService;  // Fake service for testing
    
    /**
     * TEST 1: Health endpoint returns UP
     * 
     * This is the most critical test - if health fails,
     * Ansible thinks the app is dead and tries to restart it!
     */
    @Test
    @DisplayName("Health endpoint should return UP status")
    void healthEndpoint_ShouldReturnUp() throws Exception {
        // ARRANGE: Setup fake health response
        Map<String, Object> healthResponse = new HashMap<>();
        healthResponse.put("status", "UP");
        healthResponse.put("uptime", "00:05:30");
        
        when(deployService.getHealthStatus()).thenReturn(healthResponse);
        
        // ACT & ASSERT: Call /api/health and verify response
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
    
    /**
     * TEST 2: Deploy trigger returns a build ID
     * 
     * When we POST to /api/deploy, we should get back a unique
     * build ID that can be used to track the deployment.
     */
    @Test
    @DisplayName("Deploy trigger should return build ID")
    void deployTrigger_ShouldReturnBuildId() throws Exception {
        // ARRANGE: Service returns a build ID
        String expectedBuildId = "BUILD-1234567890";
        when(deployService.triggerDeploy(anyString())).thenReturn(expectedBuildId);
        
        // ACT & ASSERT: POST to /api/deploy
        mockMvc.perform(post("/api/deploy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.buildId").value(expectedBuildId));
    }
    
    /**
     * TEST 3: Ping endpoint works
     * 
     * Simple sanity check - if this fails, something is very wrong!
     */
    @Test
    @DisplayName("Ping endpoint should return pong")
    void pingEndpoint_ShouldReturnPong() throws Exception {
        mockMvc.perform(get("/api/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("pong"))
                .andExpect(jsonPath("$.app").value("NeoDeploy"));
    }
    
    /**
     * TEST 4: Dashboard endpoint returns data
     */
    @Test
    @DisplayName("Dashboard should return stats")
    void dashboardEndpoint_ShouldReturnStats() throws Exception {
        // ARRANGE
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalDeploys", 10);
        dashboardData.put("successCount", 9);
        dashboardData.put("failedCount", 1);
        dashboardData.put("successRate", 90.0);
        
        when(deployService.getDashboardStats()).thenReturn(dashboardData);
        
        // ACT & ASSERT
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDeploys").value(10))
                .andExpect(jsonPath("$.successRate").value(90.0));
    }
}
