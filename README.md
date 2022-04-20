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
docker run -d -p 13306:3306 --env MARIADB_ROOT_PASSWORD=root mariadb:latest
```

[Vertica](https://hub.docker.com/r/vertica/vertica-ce)
```
docker run -d -p 15433:5433 --env APP_DB_USER=app --env APP_DB_PASSWORD=pswd vertica/vertica-ce
```
*WARNING:* The vertica image is big, make sure your docker has plenty of space

# Spring tests not working
So now the problems I am hitting. Basically I just want a test context with all services and datasources automatically configured. In my testing it seems fine when all the auto-configure stuff is in the same module, but now that it's split over two (itest-lib and itest-app) there seems to be issues.

The integration testes us the `itest` profile to start mariadb and vertica containers for use in the testing.
```
mvn -P itest verify
```

## Does not load config from main
So my ideal situation is just to have test specific config in [application-itest.yml](itest-app/src/test/resources/application-itest.yml) in the `test` directory, and have all the generic config read from [application.yml](itest-app/src/main/resources/application.yml) in the `main` directory. But the `main` config does not appear to be read, so I have to create a [copy of it](itest-app/src/test/resources/application.yml) which is dodgey because they could (and most likely will) get out of sync. 

## Vertica DataSource bean not found
This is strange. The test class `@Autowire` of the vertica Datasource fails.
```
[ERROR] testApplicationLoaded  Time elapsed: 0 s  <<< ERROR!
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'com.sodved.itesteg.app.itest.IntegrationIT': Unsatisfied dependency expressed through field 'vertica'; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'javax.sql.DataSource' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Qualifier(value=verticaDataSource), @org.springframework.beans.factory.annotation.Autowired(required=true)}
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'javax.sql.DataSource' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Qualifier(value=verticaDataSource), @org.springframework.beans.factory.annotation.Autowired(required=true)}
```
What is really weird is that it works if I move the [VerticaConfig](itest-app/src/main/java/com/sodved/itesteg/app/config/VerticaConfig.java) class into the `itest-lib` module it will then work.

You can see this in the [vertica-config-in-lib branch](https://github.com/sodved/itest-example/tree/vertica-config-in-lib). This is not a valid workaround for the production case I am mimicking, not every module which uses `lib` will have a vertica environment available.

Why does the location of the source file matter?

## Application Class Not Found
So this is probably related, but even weirder. If my test class wants to use a service from `itest-app` module the test class will not load with a `ClassNotFoundException`.
```
[ERROR] com.sodved.itesteg.app.itest.IntegrationIT  Time elapsed: 0.003 s  <<< ERROR!
java.lang.NoClassDefFoundError: Lcom/sodved/itesteg/app/service/ApplicationTableService;
Caused by: java.lang.ClassNotFoundException: com.sodved.itesteg.app.service.ApplicationTableService
```
Weird in that no stack trace or anything. Everything compiled OK.

If I use a service class from the lib module it works ok. So in conjunction with the above point it appears that SprintBootTest is somehow not acknowledging the classes in `itest-app` module (package `com.sodved.itesteg.app`), only the ones in the `itest-lib` module (package `com.sodved.itesteg.lib`).

In the [test context](https://github.com/sodved/itest-example/blob/vertica-config-in-lib/itest-app/src/test/java/com/sodved/itesteg/app/itest/DummyApplicationContext.java#L7) I tell it to scan all `com.sodved.itesteg` packages. This is what the main app does and it all works fine.

