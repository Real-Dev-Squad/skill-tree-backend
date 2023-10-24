# skill-tree-backend

## Steps for running the service in local
_A detailed README is written by @shubham sidgar, this is written temporarily for people to get their backend running, more details in this ticket #25_


### Tools required
- [Maven](https://mvnrepository.com/)
- Java
- IDE
- Docker desktop
- Mysql docker image

### Steps for connecting the service to the Mysql running in docker
This [link](https://find10archived.medium.com/how-to-connect-a-mysql-docker-container-with-a-local-spring-boot-application-9366707dce0d) can be used as reference for the steps
- Create a database user with all access to create tables, delete and drop tables [This is for development only in local MySql]
- After creating the database user, enter the credentials in the Skill tree spring boot application
- Try connecting the database on this URL = "jdbc:mysql://${MYSQL_HOST:localhost}:3306/${DB_NAME}"
- Try writing a simple API to test out the database connection and the data retrival
