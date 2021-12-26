# Investment Portfolio
An Investment Portfolio web-app to keep track of all your investments and watch stocks.

To run this app, you need to do the following steps.

## 1. Build the backend.

Create an ```application.properties``` file in ```src/main/resources```.
For example:
```
server.port=8080
spring.jpa.database=POSTGRESQL
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://host:5432/database
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```

Then create a ```gradle-wrapper.properties``` file in ```gradle/wrapper```.
For example:
```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-7.3.2-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

After that you can build the jar file using
```
gradle build
```
Or, directly run the application using
```
gradle bootRun
```

## 2. Building the frontend
You can build the frontend using the following set commands:
```
npm install
npm run build
```
After that you can install any server of your choice (like nginx) to serve the build folder.

Or, you can run the frontend for developemt using:
```
npm start
```

Visit ```http://localhost:3000``` to view the webapp.
