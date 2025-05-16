package com.RDS.skilltree;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestContainerManager {
    @Value("${test.db.mysql-image}")
    private String MYSQL_IMAGE_NAME;

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mySQLContainer() {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(MYSQL_IMAGE_NAME);
        mysqlContainer.start();
        return mysqlContainer;
    }
}
