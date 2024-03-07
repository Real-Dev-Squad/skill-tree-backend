package com.RDS.skilltree.integration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

abstract class TestContainerManager {
    @ServiceConnection
    static final MySQLContainer mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer("mysql:8.1")
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
