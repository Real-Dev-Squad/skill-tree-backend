
# Skill Tree Backend

## Steps to Run the Service Locally

### Required Tools

- [Maven](https://maven.apache.org/download.cgi) version 3.9.6 or higher

Installing Maven on macOS using Homebrew:
1. Open your terminal.
2. Type the following command and press Enter to install Homebrew (if not installed):
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

3. Once Homebrew is installed, run the following command to install Maven:
brew install maven

4. Wait for the installation process to complete.

5. Verify the Maven installation by typing:
mvn -version


This should display information about the installed Maven version.

Installing Maven on Windows:

1. To install Maven on Windows, we head over to the Apache Maven site to download the latest version and select the Maven zip file, for example, apache-maven-3.9.6-bin.zip.

2. Adding Maven to the Environment Path
We add both M2_HOME and MAVEN_HOME variables to the Windows environment using system properties and point them to our Maven folder.

3. Verify the Maven installation by typing:
mvn -version

Then, we update the PATH variable by appending the Maven bin folder — %M2_HOME%\bin — so that we can run the Maven command everywhere.

Then, we unzip it to the folder where we want Maven to live.


- Java (version 17 or higher) [Link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Any text editor of your choice (Preferred - IntelliJ Community or Ultimate Edition).
- Docker Desktop: [Installation steps](https://docs.docker.com/engine/install/)
- MySQL Docker Image:  [Installation steps](https://hub.docker.com/_/mysql)


### Steps to Login to MySQL

1. Go to Docker Desktop

2. You'll see skill-tree-backed (If the process is running)

3. skill-tree-backend>skill-tree-backend-db-1>open in terminal
 
to login to MySQL
mysql -u root -p (in terminal)
password : rootpassword

Refrence Screenshots:
If the project is started with docker compose up thise can be seen once you open Docker Desktop:
<img width="1680" alt="Screenshot 2023-12-26 at 9 33 17 PM" src="https://github.com/ashifkhn/skill-tree-backend/assets/54736284/57b90473-ae22-45b2-8a19-3377bfbcf1b9">


Terminal needs to be opened here:
![image](https://github.com/ashifkhn/skill-tree-backend/assets/54736284/d66166ae-b931-40ab-914f-f42615323a32)



## Steps for Creating the Database

1. `create database skilltree;`(semicolon is important here)
2. `show databases;`
3. `create user 'testuser' identified by 'testpassword';` (Username: testuser, Password: testpassword)
4. `grant all on skilltree.* to testuser;`

## Steps for Creating the Database

1. After creating the database project needs to be compiled.
2. Open skill-tree-backend in intellij.
3. Java_Home path needs to be added here.
4. You can either add the existing path and jdk 17 can be downloaded inside intellij.


(Below steps are not required as of now.)

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

1. If you've already generated the public and private keys during the website backend setup, you can proceed directly to step 5.
2. Ensure the website-backend is running locally.
3. Generate Public and Private keys using openssl with the following commands:
   - `openssl genrsa -out ./private.key 4096`
   - `openssl rsa -in private.key -pubout -outform PEM -out public.key`
4. Obtain a user token (private and public key stored in `local.js` file).
5. After calling `localhost:3000/auth/github/login`, the backend will generate the JWT token.
6. Validate the token using [jwt.io](https://jwt.io/) by entering the public and private keys stored in the website backend.
7. Use the public key in the Skill Tree repo to decrypt the JWT token passed for authentication.


## Steps for connecting mysql workbench to run mysql inside docker

1. `docker exec -it rds-db-1 bin/bash`
2. bash-4.4# `mysql -u root -p -A`

By default after deployment MySQL has following connection restrictions:
```
mysql> select host, user from mysql.user;
+-----------+---------------+
| host      | user          |
+-----------+---------------+
| localhost | healthchecker |
| localhost | mysql.session |
| localhost | mysql.sys     |
| localhost | root          |
+-----------+---------------+
4 rows in set (0.00 sec)
```
Apparently, for security purposes, you will not be able to connect to it from outside of the docker container. If you need to change that to allow root to connect from any host (say, for development purposes), do the following:

3. update mysql.user set host='%' where user='root';
4. Quit the mysql client.
5. Restart the docker container

Now you can connect to the mysql running in the docker container, also to connect to it on the terminal type `mysql -utestuser -p -P3306 -h127.0.0.1`

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
   Note : Publickey in both backend and skilltree backend should be the same.
3. Click "Edit Configurations" -> Create a new application.
4. Give it a name instead of "Unnamed".
5. In "Build and Run", select Java 17.
6. In the class , check "Enable Env file" and "Substitute env var" checkboxes.
7. Click "Apply" and "OK".
8. Click "Build and Run".
9. Retrieve the Bearer token by accessing `http://localhost:3000/auth/github/login` and locating the key `rds-session-development` in the application. The value associated with this key is the `Bearer token`.
10. Click the green "Run" button or "Shift + F10" to start the application
11. After starting the Tomcat server on port `8080`, attempt to access the dummy route `http://localhost:8080/test` using the `GET` method in Postman or ThunderClient while providing the `bearer token`. If the terminal displays `test123`, it indicates that the setup has been successful.

## Known Issues Faced by Other Developers
1. Port 8080 Conflict: Make sure there is no other process running on the 8080 port where we are going to run our server check this with lsof -p PID (PID - port id)
2. Local MySQL Conflict: Make sure there is no local Mysql running on the local machine

---
