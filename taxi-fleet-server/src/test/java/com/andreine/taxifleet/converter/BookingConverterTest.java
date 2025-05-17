package com.andreine.taxifleet.converter;

import java.time.Instant;

import com.andreine.taxifleet.controller.common.model.LocationDto;
import com.andreine.taxifleet.controller.publicapi.model.BookingRequest;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.service.model.Booking;
import com.andreine.taxifleet.service.model.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingConverterTest {

    @Test
    void shouldConvertEntityToDomain() {
        var bookingEntity = BookingEntity.builder()
            .id(1L)
            .userId(2L)
            .originLongitude(10.0)
            .originLatitude(20.0)
            .destinationLongitude(30.0)
            .destinationLatitude(40.0)
            .status(BookingStatus.AVAILABLE)
            .taxiId(1L)
            .createdOn(Instant.ofEpochMilli(1000L))
            .updatedOn(Instant.ofEpochMilli(2000L))
            .build();

        var booking = BookingConverter.fromEntity(bookingEntity);

        assertThat(booking.id()).isEqualTo(1L);
        assertThat(booking.userId()).isEqualTo(2L);
        assertThat(booking.fromLocation().latitude()).isEqualTo(20.0);
        assertThat(booking.fromLocation().longitude()).isEqualTo(10.0);
        assertThat(booking.toLocation().latitude()).isEqualTo(40.0);
        assertThat(booking.toLocation().longitude()).isEqualTo(30.0);
        assertThat(booking.toLocation().latitude()).isEqualTo(40.0);
        assertThat(booking.status()).isEqualTo(Booking.BookingStatus.AVAILABLE);
        assertThat(booking.taxiId()).isEqualTo(1L);
        assertThat(booking.createdOnTs()).isEqualTo(1000L);
        assertThat(booking.updatedOnTs()).isEqualTo(2000L);
    }

    @Test
    void convertConvertEntityToDomainWithNullUpdatedOn() {
        var bookingEntity = BookingEntity.builder()
            .id(1L)
            .userId(2L)
            .originLongitude(10.0)
            .originLatitude(20.0)
            .destinationLongitude(30.0)
            .destinationLatitude(40.0)
            .status(BookingStatus.AVAILABLE)
            .taxiId(1L)
            .createdOn(Instant.ofEpochMilli(1000L))
            .build();

        var booking = BookingConverter.fromEntity(bookingEntity);

        assertThat(booking.updatedOnTs()).isNull();
    }

    @Test
    void convertConvertDomainToDto() {
        var booking = Booking.builder()
            .id(1L)
            .userId(2L)
            .fromLocation(new Location(10.0, 20.0))
            .toLocation(new Location(30.0, 40.0))
            .status(Booking.BookingStatus.AVAILABLE)
            .taxiId(1L)
            .createdOnTs(100500L)
            .updatedOnTs(100500L)
            .build();

        var bookingDto = BookingConverter.toDto(booking);

        assertThat(bookingDto.id()).isEqualTo(1L);
        assertThat(bookingDto.userId()).isEqualTo(2L);
        assertThat(bookingDto.fromLocation().getLatitude()).isEqualTo(10.0);
        assertThat(bookingDto.fromLocation().getLongitude()).isEqualTo(20.0);
        assertThat(bookingDto.toLocation().getLatitude()).isEqualTo(30.0);
        assertThat(bookingDto.toLocation().getLongitude()).isEqualTo(40.0);
        assertThat(bookingDto.status()).isEqualTo("AVAILABLE");
        assertThat(bookingDto.taxiId()).isEqualTo(1L);
        assertThat(bookingDto.createdOnSeconds()).isEqualTo(100L);
    }

    @Test
    void convertConvertDomainToEntity() {
        var booking = Booking.builder()
            .id(1L)
            .userId(2L)
            .fromLocation(new Location(10.0, 20.0))
            .toLocation(new Location(30.0, 40.0))
            .status(Booking.BookingStatus.AVAILABLE)
            .taxiId(1L)
            .createdOnTs(100500L)
            .updatedOnTs(100500L)
            .build();

        var bookingEntity = BookingConverter.toEntity(booking);

        assertThat(bookingEntity.getId()).isEqualTo(1L);
        assertThat(bookingEntity.getUserId()).isEqualTo(2L);
        assertThat(bookingEntity.getOriginLatitude()).isEqualTo(10.0);
        assertThat(bookingEntity.getOriginLongitude()).isEqualTo(20.0);
        assertThat(bookingEntity.getDestinationLatitude()).isEqualTo(30.0);
        assertThat(bookingEntity.getDestinationLongitude()).isEqualTo(40.0);
        assertThat(bookingEntity.getStatus()).isEqualTo(BookingStatus.AVAILABLE);
        assertThat(bookingEntity.getTaxiId()).isEqualTo(1L);
    }

    @Test
    void convertConvertApiRequestToDomain() {
        var bookingRequest = BookingRequest.builder()
            .userId(2L)
            .fromLocation(LocationDto.builder()
                .latitude(10.0)
                .longitude(20.0)
                .build())
            .toLocation(LocationDto.builder()
                .latitude(30.0)
                .longitude(40.0)
                .build())
            .build();

        var booking = BookingConverter.fromRequest(bookingRequest);

        assertThat(booking.userId()).isEqualTo(2L);
        assertThat(booking.fromLocation().latitude()).isEqualTo(10.0);
        assertThat(booking.fromLocation().longitude()).isEqualTo(20.0);
        assertThat(booking.toLocation().latitude()).isEqualTo(30.0);
        assertThat(booking.toLocation().longitude()).isEqualTo(40.0);
        assertThat(booking.status()).isEqualTo(Booking.BookingStatus.AVAILABLE);
    }

}
