package com.neodeploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NeoDeploy Application - CI/CD Pipeline Demo
 * 
 * This is the main entry point of our Spring Boot application.
 * @SpringBootApplication does 3 things:
 *   1. @Configuration - This class can define beans
 *   2. @EnableAutoConfiguration - Spring auto-configures based on dependencies
 *   3. @ComponentScan - Scans this package and sub-packages for components
 */
@SpringBootApplication
public class NeodeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeodeployApplication.class, args);
        System.out.println("🚀 NeoDeploy is running at http://localhost:8080");
        System.out.println("📊 Dashboard: http://localhost:8080/index.html");
        System.out.println("❤️ Health: http://localhost:8080/actuator/health");
    }
}
