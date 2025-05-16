package com.andreine.taxifleet.converter;

import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.controller.publicapi.model.BookingDto;
import com.andreine.taxifleet.persistence.model.BookingEntity;
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
    public static Booking convert(BookingEntity bookingEntity) {
        return Booking.builder()
            .id(bookingEntity.getId())
            .userId(bookingEntity.getUserId())
            .fromLocation(Location.of(bookingEntity.getOriginLatitude(), bookingEntity.getOriginLongitude()))
            .toLocation(Location.of(bookingEntity.getDestinationLatitude(), bookingEntity.getDestinationLongitude()))
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
    public static BookingDto convert(Booking booking) {
        return BookingDto.builder()
            .id(booking.id())
            .userId(booking.userId())
            .fromLocation(LocationConverter.convert(booking.fromLocation()))
            .toLocation(LocationConverter.convert(booking.toLocation()))
            .status(booking.status().name())
            .taxiId(booking.taxiId())
            .createdOnSeconds(TimeUnit.MILLISECONDS.toSeconds(booking.createdOnTs()))
            .build();
    }

}
