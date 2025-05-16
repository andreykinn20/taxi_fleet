package com.andreine.taxifleet.api;

import java.time.Instant;
import java.util.List;

import com.andreine.taxifleet.BaseFunctionalTest;
import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class AdminApiTest extends BaseFunctionalTest {

    @Test
    void shouldGetTaxis() {
        var taxi1 = taxi()
            .name("Taxi 1")
            .build();
        var taxi2 = taxi()
            .name("Taxi 2")
            .build();

        taxiRepository.saveAll(List.of(taxi1, taxi2));

        var taxis = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .get("/admin/taxis")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<TaxiDto>>() {
            });

        assertThat(taxis).hasSize(2);
        assertThat(taxis.getFirst().id()).isEqualTo(taxi1.getId());
        assertThat(taxis.getFirst().name()).isEqualTo("Taxi 1");
        assertThat(taxis.getFirst().location().latitude()).isEqualTo(2.0);
        assertThat(taxis.getFirst().location().longitude()).isEqualTo(1.0);
        assertThat(taxis.getFirst().status()).isEqualTo("AVAILABLE");
        assertThat(taxis.getFirst().registeredOnSeconds()).isEqualTo(100L);
        assertThat(taxis.getLast().id()).isEqualTo(taxi2.getId());
        assertThat(taxis.getLast().name()).isEqualTo("Taxi 2");
        assertThat(taxis.getLast().location().latitude()).isEqualTo(2.0);
        assertThat(taxis.getLast().location().longitude()).isEqualTo(1.0);
        assertThat(taxis.getLast().status()).isEqualTo("AVAILABLE");
        assertThat(taxis.getLast().registeredOnSeconds()).isEqualTo(100L);
    }

    private TaxiEntity.TaxiEntityBuilder taxi() {
        return TaxiEntity.builder()
            .status(TaxiStatus.AVAILABLE)
            .longitude(1.0)
            .latitude(2.0)
            .name("Taxi")
            .registeredOn(Instant.ofEpochMilli(100500L));
    }

}
