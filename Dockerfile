# build
FROM maven:3.9.6-amazoncorretto-21 AS builder
WORKDIR /app
COPY . /app/
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# runtime
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/*.jar"]