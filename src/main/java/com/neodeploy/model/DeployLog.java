package com.neodeploy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * DeployLog Entity - Stores history of all deployments
 * 
 * WHY THIS EXISTS:
 * Every time we deploy, we save a record. This lets us:
 * - Track deployment history
 * - Show success/failure stats on dashboard
 * - Debug issues by looking at past deployments
 * 
 * @Entity tells JPA this maps to a database table
 * @Data (Lombok) auto-generates getters, setters, toString, equals, hashCode
 */
@Entity
@Table(name = "deploy_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeployLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "build_id", nullable = false, unique = true)
    private String buildId;  // Unique ID like "BUILD-1712345678"
    
    @Column(nullable = false)
    private String status;   // PENDING, BUILDING, TESTING, DEPLOYING, SUCCESS, FAILED
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "triggered_by")
    private String triggeredBy;  // Who started this? "jenkins", "manual", "api"
    
    @Column(length = 2000)
    private String message;  // Success message or error details
    
    @Column(name = "commit_hash")
    private String commitHash;  // Git commit that was deployed
    
    @Column(name = "docker_image")
    private String dockerImage;  // Docker image tag that was built
    
    // Constructor for quick creation
    public DeployLog(String buildId, String status) {
        this.buildId = buildId;
        this.status = status;
        this.startTime = LocalDateTime.now();
    }
}
