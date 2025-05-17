package com.andreine.taxifleet.api;

import java.time.Instant;
import java.util.List;

import com.andreine.taxifleet.BaseFunctionalTest;
import com.andreine.taxifleet.controller.common.model.LocationDto;
import com.andreine.taxifleet.controller.publicapi.model.BookingDto;
import com.andreine.taxifleet.controller.publicapi.model.BookingRequest;
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

class PublicApiTest extends BaseFunctionalTest {

    @Test
    void shouldGetAvailableBookings() {
        var booking = BookingEntity.builder()
            .userId(1L)
            .status(BookingStatus.AVAILABLE)
            .originLatitude(1.0)
            .originLongitude(2.0)
            .destinationLatitude(3.0)
            .destinationLongitude(4.0)
            .build();

        var savedBooking = bookingRepository.save(booking);

        var availableBookings = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .get("/public/bookings/available")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<BookingDto>>() {
            });

        assertThat(availableBookings).hasSize(1);
        assertThat(availableBookings.getFirst().id()).isEqualTo(savedBooking.getId());
        assertThat(availableBookings.getFirst().userId()).isEqualTo(1L);
        assertThat(availableBookings.getFirst().fromLocation().getLatitude()).isEqualTo(1.0);
        assertThat(availableBookings.getFirst().fromLocation().getLongitude()).isEqualTo(2.0);
        assertThat(availableBookings.getFirst().toLocation().getLatitude()).isEqualTo(3.0);
        assertThat(availableBookings.getFirst().toLocation().getLongitude()).isEqualTo(4.0);
        assertThat(availableBookings.getFirst().status()).isEqualTo("AVAILABLE");
        assertThat(availableBookings.getFirst().createdOnSeconds()).isNotNull();
    }

    @Test
    void shouldAcceptBooking() {
        var booking = booking()
            .status(BookingStatus.AVAILABLE)
            .build();
        var taxi = taxi()
            .status(TaxiStatus.AVAILABLE)
            .build();

        var savedBooking = bookingRepository.save(booking);
        var savedTaxi = taxiRepository.save(taxi);

        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .post("/public/taxi/{taxiId}/bookings/{bookingId}/accept", savedTaxi.getId(), savedBooking.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(bookingRepository.findById(savedBooking.getId()))
            .hasValueSatisfying(acceptedBooking -> {
                assertThat(acceptedBooking.getTaxiId()).isEqualTo(savedTaxi.getId());
                assertThat(acceptedBooking.getStatus()).isEqualTo(BookingStatus.ACCEPTED);
            });
        assertThat(taxiRepository.findById(savedTaxi.getId()))
            .hasValueSatisfying(acceptedTaxi -> {
                assertThat(acceptedTaxi.getStatus()).isEqualTo(TaxiStatus.BOOKED);
            });
    }

    @Test
    void shouldGetTaxiBookings() {
        var taxi1 = taxi()
            .name("Taxi A")
            .registeredOn(Instant.ofEpochMilli(100500))
            .build();
        var taxi2 = taxi()
            .name("Taxi B")
            .registeredOn(Instant.ofEpochMilli(100500))
            .build();
        var savedTaxis = taxiRepository.saveAll(List.of(taxi1, taxi2));

        var booking1 = booking()
            .build();
        var booking2 = booking()
            .taxiId(savedTaxis.getFirst().getId())
            .build();
        var booking3 = booking()
            .taxiId(savedTaxis.getLast().getId())
            .build();
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        var taxiBookings = given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .get("/public/taxi/{taxiId}/bookings", savedTaxis.getFirst().getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<BookingDto>>() {
            });

        assertThat(taxiBookings).hasSize(1);
        assertThat(taxiBookings.getFirst().id()).isEqualTo(booking2.getId());
    }

    @Test
    void shouldSetTaxiUnavailable() {
        var taxi = taxi()
            .status(TaxiStatus.AVAILABLE)
            .build();

        var savedTaxi = taxiRepository.save(taxi);

        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .post("/public/taxi/{taxiId}/status/unavailable", savedTaxi.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taxiRepository.findById(savedTaxi.getId()))
            .hasValueSatisfying(unavailableTaxi -> {
                assertThat(unavailableTaxi.getId()).isEqualTo(savedTaxi.getId());
                assertThat(unavailableTaxi.getStatus()).isEqualTo(TaxiStatus.UNAVAILABLE);
            });
    }

    @Test
    void shouldSetTaxiAvailable() {
        var taxi = taxi()
            .status(TaxiStatus.UNAVAILABLE)
            .build();

        var savedTaxi = taxiRepository.save(taxi);

        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .post("/public/taxi/{taxiId}/status/available", savedTaxi.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taxiRepository.findById(savedTaxi.getId()))
            .hasValueSatisfying(unavailableTaxi -> {
                assertThat(unavailableTaxi.getId()).isEqualTo(savedTaxi.getId());
                assertThat(unavailableTaxi.getStatus()).isEqualTo(TaxiStatus.AVAILABLE);
            });
    }

    @Test
    void shouldCompleteBooking() {
        var booking = booking()
            .status(BookingStatus.ACCEPTED)
            .build();
        var taxi = taxi()
            .status(TaxiStatus.BOOKED)
            .build();

        var savedBooking = bookingRepository.save(booking);
        var savedTaxi = taxiRepository.save(taxi);

        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .post("/public/taxi/{taxiId}/bookings/{bookingId}/complete", savedTaxi.getId(), savedBooking.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(bookingRepository.findById(savedBooking.getId()))
            .hasValueSatisfying(acceptedBooking -> {
                assertThat(acceptedBooking.getStatus()).isEqualTo(BookingStatus.COMPLETED);
            });
        assertThat(taxiRepository.findById(savedTaxi.getId()))
            .hasValueSatisfying(acceptedTaxi -> {
                assertThat(acceptedTaxi.getStatus()).isEqualTo(TaxiStatus.AVAILABLE);
            });
    }

    @Test
    void shouldRegisterBooking() {
        var taxi = taxi()
            .status(TaxiStatus.AVAILABLE)
            .build();
        var bookingRequest = BookingRequest.builder()
            .userId(1L)
            .toLocation(LocationDto.builder()
                .latitude(10.0)
                .longitude(20.0)
                .build())
            .fromLocation(LocationDto.builder()
                .latitude(30.0)
                .longitude(40.0)
                .build())
            .build();

        var savedTaxi = taxiRepository.save(taxi);

        given()
            .when()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(bookingRequest)
            .log().ifValidationFails()
            .post("/public/bookings")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
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

    private TaxiEntity.TaxiEntityBuilder taxi() {
        return TaxiEntity.builder()
            .name("Taxi")
            .status(TaxiStatus.AVAILABLE)
            .registeredOn(Instant.ofEpochMilli(100500));
    }

}
