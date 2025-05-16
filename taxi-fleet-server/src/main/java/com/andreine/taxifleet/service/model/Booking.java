package com.andreine.taxifleet.service.model;

import lombok.Builder;

/**
 * Booking.
 */
@Builder
public record Booking(
    long id,
    long userId,
    Location fromLocation,
    Location toLocation,
    Long taxiId,
    BookingStatus status,
    long createdOnTs,
    Long updatedOnTs
) {

    /**
     * Booking status.
     */
    public enum BookingStatus {
        AVAILABLE,
        BOOKED,
        FINISHED
    }

}
