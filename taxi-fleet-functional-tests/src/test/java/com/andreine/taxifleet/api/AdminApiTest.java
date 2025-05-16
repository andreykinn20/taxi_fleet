package com.andreine.taxifleet.api;

import java.time.Instant;
import java.util.List;

import com.andreine.taxifleet.BaseFunctionalTest;
import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
import com.andreine.taxifleet.model.MonthlyBookingStats;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
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

    @Test
    void shouldGetMonthlyBookingStats() {
        var booking1 = booking()
            .status(BookingStatus.ACCEPTED)
            .build();
        var booking2 = booking()
            .status(BookingStatus.CANCELLED)
            .build();
        var booking3 = booking()
            .status(BookingStatus.COMPLETED)
            .build();
        var booking4 = booking()
            .status(BookingStatus.COMPLETED)
            .build();

        bookingRepository.saveAll(List.of(booking1, booking2, booking3, booking4));

        var monthlyBookingStats = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .get("/admin/bookings-by-month")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<MonthlyBookingStats>>() {
            });

        assertThat(monthlyBookingStats).hasSize(1);
        assertThat(monthlyBookingStats.getFirst().completedBookings()).isEqualTo(2);
        assertThat(monthlyBookingStats.getFirst().cancelledBookings()).isEqualTo(1);
        assertThat(monthlyBookingStats.getFirst().totalBookings()).isEqualTo(4);
    }

    private TaxiEntity.TaxiEntityBuilder taxi() {
        return TaxiEntity.builder()
            .status(TaxiStatus.AVAILABLE)
            .longitude(1.0)
            .latitude(2.0)
            .name("Taxi")
            .registeredOn(Instant.ofEpochMilli(100500L));
    }

    private BookingEntity.BookingEntityBuilder booking() {
        return BookingEntity.builder()
            .userId(1L)
            .status(BookingStatus.AVAILABLE)
            .originLatitude(1.0)
            .originLongitude(2.0)
            .destinationLatitude(3.0)
            .destinationLongitude(4.0);
    }

}
