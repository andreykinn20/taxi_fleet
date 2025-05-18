FROM maven:3.9.6-amazoncorretto-21 AS builder
COPY pom.xml /tmp/
COPY taxi-fleet-bundle /tmp/taxi-fleet-bundle/
COPY taxi-fleet-liquibase /tmp/taxi-fleet-liquibase/
COPY taxi-fleet-server /tmp/taxi-fleet-server/
COPY taxi-fleet-functional-tests /tmp/taxi-fleet-functional-tests/
WORKDIR /tmp/
RUN mvn clean install -Pdocker -Dmaven.test.skip=true

FROM amazoncorretto:21-alpine
COPY --from=builder /tmp/taxi-fleet-bundle/target/taxi-fleet-jar-with-dependencies.jar app.jar

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]