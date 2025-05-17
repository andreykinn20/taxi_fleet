package com.andreine.taxifleet.converter;

import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.controller.publicapi.model.BookingDto;
import com.andreine.taxifleet.controller.publicapi.model.BookingRequest;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.service.model.Booking;
import com.andreine.taxifleet.service.model.Location;
import lombok.experimental.UtilityClass;

/**
 * Booking converter.
 */
@UtilityClass
public class BookingConverter {

    /**
     * Converts booking entity to booking.
     *
     * @param bookingEntity booking entity
     * @return booking
     */
    public static Booking fromEntity(BookingEntity bookingEntity) {
        return Booking.builder()
            .id(bookingEntity.getId())
            .userId(bookingEntity.getUserId())
            .fromLocation(new Location(bookingEntity.getOriginLatitude(), bookingEntity.getOriginLongitude()))
            .toLocation(new Location(bookingEntity.getDestinationLatitude(), bookingEntity.getDestinationLongitude()))
            .status(Booking.BookingStatus.valueOf(bookingEntity.getStatus().name()))
            .taxiId(bookingEntity.getTaxiId())
            .createdOnTs(bookingEntity.getCreatedOn().toEpochMilli())
            .updatedOnTs(bookingEntity.getUpdatedOn() != null ? bookingEntity.getUpdatedOn().toEpochMilli() : null)
            .build();
    }

    /**
     * Converts booking to booking dto.
     *
     * @param booking booking
     * @return booking dto
     */
    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
            .id(booking.id())
            .userId(booking.userId())
            .fromLocation(LocationConverter.toDto(booking.fromLocation()))
            .toLocation(LocationConverter.toDto(booking.toLocation()))
            .status(booking.status().name())
            .taxiId(booking.taxiId())
            .createdOnSeconds(TimeUnit.MILLISECONDS.toSeconds(booking.createdOnTs()))
            .build();
    }

    /**
     * Converts booking to booking entity.
     *
     * @param booking booking
     * @return booking
     */
    public static BookingEntity toEntity(Booking booking) {
        return BookingEntity.builder()
            .id(booking.id())
            .userId(booking.userId())
            .originLatitude(booking.fromLocation().latitude())
            .originLongitude(booking.fromLocation().longitude())
            .destinationLatitude(booking.toLocation().latitude())
            .destinationLongitude(booking.toLocation().longitude())
            .status(BookingStatus.valueOf(booking.status().name()))
            .taxiId(booking.taxiId())
            .build();
    }

    /**
     * Converts booking request to booking.
     *
     * @param bookingRequest booking request
     * @return booking
     */
    public static Booking fromRequest(BookingRequest bookingRequest) {
        return Booking.builder()
            .userId(bookingRequest.getUserId())
            .fromLocation(new Location(bookingRequest.getFromLocation().getLatitude(), bookingRequest.getFromLocation().getLongitude()))
            .toLocation(new Location(bookingRequest.getToLocation().getLatitude(), bookingRequest.getToLocation().getLongitude()))
            .status(Booking.BookingStatus.AVAILABLE)
            .build();
    }

}
