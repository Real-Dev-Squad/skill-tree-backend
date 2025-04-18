package com.RDS.skilltree;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

public abstract class TestContainerManager {
    @ServiceConnection static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1.0");

    static {
        mySQLContainer.start();
    }
}
