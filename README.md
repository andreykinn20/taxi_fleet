# Taxi Fleet
----
This project is a practice project, utilizing docker-compose and Dockerfile to build a maven/Spring-boot project.

Application provides a REST API to manage a taxi fleet. It allows you to manage bookings, as well as retrieve information about the fleet.

The main objective is to manage the entire build for a complex maven project in a single command, `docker-compose up`.

__Usage__

`docker-compose up` to build image and start container. Building the image automatically runs tests.

`GET http://localhost:8080/swagger-ui/index.html` leads to the swagger page

`docker-compose down` will close the container

`docker-compose down -v --rmi 'all'` will close the container and delete the old image