package com.andreine.taxifleet.api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.andreine.taxifleet.BaseFunctionalTest;
import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

class TaxiFleetPublicApiTest extends BaseFunctionalTest {

    @Test
    void shouldSave() {
        var order = OrderEntity.builder()
            .userId(1L)
            .status(OrderStatus.AVAILABLE)
            .originLatitude(2.0)
            .originLongitude(2.0)
            .destinationLatitude(1.0)
            .destinationLongitude(1.0)
            .build();

        orderRepository.save(order);

        var availableOrders = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .get("/public/taxi/{taxiId}/orders/available", 1L)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(List.class);
    }
}
