# The Resident Zombie
Rest API to catalog the survivors of a zombie apocalypse

### Getting Started
#### Running in development
    Pass the VM Argument: -Dspring.profiles.active=dev
Note: Running the application in development profile automatically populate the database with items and some survivors
####
    To check the database access:
    http://localhost:8080/h2-console
    You will find the database url, username and password
    in application-dev.properties file

#### Running in production
    Pass the VM Arguments: 
    -Dspring.profiles.active=prod
    -Dspring.datasource.url={YOUR_DATABASE_URL}
    -Dspring.datasource.username={YOUR_DATABASE_USERNAME}
    -Dspring.datasource.password={YOUR_DATABASE_PASSWORD}
Note: Running the application in production profile there are two things to keep in mind, to flyway do the database migration of tables you need to create the database first and the database is automatically populated only with items

#### Once the application is up you can test the endpoints of it by accessing the swagger endpoint
    http://localhost:8080/swagger-ui.html

Note: All the url examples are assuming the spring will run in its default port, if you want to change it feel free to do it passing the VM argument:
####
    -Dserver.port={YOUR_PORT}    
    