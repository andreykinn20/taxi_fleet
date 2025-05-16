package com.andreine.taxifleet.integration.kafka.producer.model;

import com.andreine.taxifleet.service.model.Location;
import lombok.Builder;

/**
 * Booking message DTO.
 */
@Builder
public record BookingMessageDto(
    long bookingId,
    long taxiId,
    long userId,
    Location fromLocation,
    Location toLocation,
    long createdOnTs
) {

}
