package com.example.mushrooming;

import org.testcontainers.containers.MySQLContainer;

public class DBTestConfig {
    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("mushrooming_test")
            .withUsername("testuser")
            .withPassword("pass")
            .withReuse(true);

    static {
        mySQLContainer.start();
    }
}
