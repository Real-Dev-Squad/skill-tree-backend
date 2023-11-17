
# Skill Tree Backend

## Steps to Run the Service Locally

### Required Tools

- [Maven](https://mvnrepository.com/)
- Java
- Integrated Development Environment (IDE)
- Docker Desktop
- MySQL Docker Image

### Steps to Connect the Service to MySQL Running in Docker

Refer to this [link](https://find10archived.medium.com/how-to-connect-a-mysql-docker-container-with-a-local-spring-boot-application-9366707dce0d) for detailed steps.

1. Create a database user with full access for table creation, deletion, and modification (for development only in local MySQL).
2. After creating the database user, enter the credentials in the Skill Tree Spring Boot application.
3. Attempt to connect to the database using the URL "jdbc:mysql://${MYSQL_HOST:localhost}:3306/${DB_NAME}".
4. Create a simple API to test the database connection and data retrieval. For example, create a `/test` route in `skill-tree/src/main/java/com/RDS/skilltree/User/UserController.java`.
    ```java
    package com.RDS.skilltree.User;

    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class UserController {
        @GetMapping("/test")
        public void test(){
            System.out.println("test123");
        }
    }
    ```

### Steps to Generate a JWT Token (Similar to [Website Backend Repo](https://github.com/Real-Dev-Squad/website-backend/issues))

1. Ensure the website-backend is running locally.
2. Generate Public and Private keys using openssl with the following commands:
   - `openssl genrsa -out ./private.key 4096`
   - `openssl rsa -in private.key -pubout -outform PEM -out public.key`
3. Obtain a user token (private and public key stored in `local.js` file).
4. After calling `localhost:3000/auth/github/login`, the backend will generate the JWT token.
5. Validate the token using [jwt.io](https://jwt.io/) by entering the public and private keys stored in the website backend.
6. Use the public key in the Skill Tree repo to decrypt the JWT token passed for authentication.

## Steps for Creating the Database

1. `create database skilltree;`
2. `show databases;`
3. `create user 'testuser' identified by 'testpassword';` (Username: testuser, Password: testpassword)
4. `grant all on skilltree.* to testuser;`

## Additional Configuration Steps

1. Download the EnvFile plugin from the marketplace.
2. Create a new Env file with the provided content and fill in the RDS public key value.
   ```env
   DB_NAME=${DB_NAME}
   MYSQL_USERNAME=testuser
   MYSQL_PASSWORD=testpassword
   DB_DDL_POLICY=update
   RDS_PUBLIC_KEY="-----BEGIN PUBLIC KEY-----
                   xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                   -----END PUBLIC KEY-----"
   ```
3. Click "Edit Configurations" -> Create a new application.
4. Give it a name instead of "Unnamed".
5. In "Build and Run", select Java 17.
6. In the class , check "Enable Env file" and "Substitute env var" checkboxes.
7. Click "Apply" and "OK".
8. Click "Build and Run".

## Known Issues Faced by Other Developers
1. Port 8080 Conflict: Make sure there is no other process running on the 8080 port where we are going to run our server check this with lsof -p PID (PID - port id)
2. Local MySQL Conflict: Make sure there is no local Mysql running on the local machine

---
