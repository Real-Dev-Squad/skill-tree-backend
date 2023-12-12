package com.RDS.skilltree;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

abstract class TestContainerManger {
    @ServiceConnection
    static final MySQLContainer mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer("mysql:latest")
                .withDatabaseName("skilltree-test")
                .withUsername("root")
                .withPassword("password");
        mysqlContainer.start();
    }
    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        
    }
}
