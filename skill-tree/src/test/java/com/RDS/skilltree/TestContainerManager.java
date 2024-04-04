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
                        "-----BEGIN PUBLIC KEY-----MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAym+XUpQxzEN3+YRiliQhj09avAD3fG80DXhMx8bbEHR/LXZ0zKwTZ0me6FHZfil/+P1rLCujRFTZuJhbGUa6fPMaj3rhcum8S2hcoKzLZCXsHLV0f2Eay0vASc1Wmz1JIJpWOZaIRjyDNzFOqgYM6QpjF9/8/Suk16zS58kxLQdMLOqXvA3ahXHvm0BtJH9NtMKra7xgmIjzepe6cfDtXMLvnFx7LaTb4e1CzY3U06cFaZCN+fNYV4q5Tz5aME1laq4ooXXQghWDG18jkuNcfliYECYmz3uZGqR/pWHkcT61TaF41fFAnKj+OE9uRB4c26+4FtnntIwyfzi5zNAgunHnvjgu9O2Sre0yBAgL/b6Jmbx0zsnzIT/RijRj2bZBKIsQvQW3/4FnCWzV2Jz+ G7mzw4A/LnF2VpY+51u0BfqAy4uCuIOmZiMqlbHYR9Kre3c1kO/vsiGjNYkBnFjQZgWGCa4se0hXyetiX4dwjXCJPaeV2zlkHEwgNSZx9peX3Dt+6w9mAztRvKUmds4mc9mn8NCryIvbFZK3ZtrMDMo3q8FwXv/CpRxOVbTuV6Zs2C2SqZAvGuY1KqtbbjjFr7h37nCBtCGZqtsS2BFe4ab/f+9DGdatAS2rqiyQhhpTmvZ4iWE9jgVwHlyVHGd+ LLwukDrVxsGQ2nL0UnYos6ECAwEAAQ==-----END PUBLIC KEY-----");
    }
}
