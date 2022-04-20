# Spring Integration Test Example

Having all sorts of issues trying to get spring integration tests running. Asked [a question on StakOverflow](https://stackoverflow.com/questions/71836098/) but all a bit convoluted help there. So decided to get the simplest example of our setup to see if I can reproduce the various issues I am hitting.

## Basic usage
This application expects a mariadb and vertica database to be running. The test app does not write any data and just reads from information tables.

So you should edit the [application-local.yml](itest-app/src/main/resources/application-local.yml) file with correct connection detail for your environment.
```
# Build app
mvn clean install

# Run app
SPRING_PROFILES_ACTIVE=local java -jar itest-app/target/itest-app-0.0.1-0-SNAPSHOT.jar
```

## Docker Databases
Developed with the default mariadb and docker databases.

[Mariadb](https://hub.docker.com/_/mariadb)
```
# Change port to whatever you require
docker run -d -p 13306:3306 --env MARIADB_ROOT_PASSWORD=root mariadb:latest
```

[Vertica](https://hub.docker.com/r/vertica/vertica-ce)
```
docker run -d -p 15433:5433 --env APP_DB_USER=app --env APP_DB_PASSWORD=pswd vertica/vertica-ce
```

# Spring tests not working
So now the problems I am hitting. Basically I just want a test context with all services and datasources automatically configured. In my testing it seems fine when all the auto-configure stuff is in the same module, but now that it's split over two (itest-lib and itest-app) there seems to be issues.

The integration testes us the `itest` profile to start mariadb and vertica containers for use in the testing.
```
mvn -P itest verify
```
Details to be added...
