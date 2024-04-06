package com.RDS.skilltree;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

abstract class TestContainerManager {
    @ServiceConnection static final MySQLContainer mysqlContainer;

    static {
        mysqlContainer =
                new MySQLContainer("mysql:8.1")
                        .withDatabaseName("skilltree-test")
                        .withUsername("root")
                        .withPassword("password");
        mysqlContainer.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("cookieName", () -> "rds-session-v2");
        registry.add(
                "jwt.rds.public.key",
                () ->
                        "-----BEGIN PUBLIC KEY----MIICITANBgkqhkiG9w0BAQEFAAOCAg4AMIICCQKCAgBpAet8sOf64PtzdnwtkZBJEJTCtQT9ZQMuuWUDXZGTG0iO7x3WZw6GanBboKGblU4VZEgd8H7bKOOIaQF4AsXsw/vUsOV5Ue73a9Jj5d57jyon7M8fFmjna3afZfb5SBru5Iv0ECePqIKUIhSmTML+y3bFKF2cbUTEe2qPK5xzBeH4AWq4Zb2N0gHNstinwrXL9LWawQPkJr23TohZEFSzyZbeklWWwz67A6YnE01w42R/TLE3LmU8YKkrHkgFsAHtUMQO++JsH4q3F9Je0VkLzj5sB5RgAYscs6YFKoFD5jKgtSRPIXz7O9GsC76dHtwGXOk47/NWxu7bUQVcD2hJYprR28PjdNk5KiRKO5Z83JkiM6ed9UAkiD/fIRI8LITaLayHdFfQvXM+dv4ugPHEq+aVllFMH1lUu/2B1aJpk4D4w5JcIzIZ9og4cMz00EGU/1o+BX2S55/Ok4MaxX6Zl3QYm1K0cPLOdisYoygPtnNEav32JLgM2yOXdyuhpYzmn66yyFFck2nnkezG5Gvlf3MejMavRO+sfIz0gDIhXEwWu0EJDrG5nmNRwejrSXx42YxmYZGkK/c82fiwbOVqIuFgsI6lWGdyDayFRg9bjrk6KiQZFnP4KcmUXk4PhiSItDJAUEkNz0+4StHNoqFhNH5pnEj4VbmJqwIDAQAB-----END PUBLIC KEY-----");
    }
}
