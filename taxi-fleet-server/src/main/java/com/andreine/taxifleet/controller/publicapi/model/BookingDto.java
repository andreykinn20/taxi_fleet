package com.andreine.taxifleet.controller.publicapi.model;

import lombok.Builder;

/**
 * Booking DTO.
 */
@Builder
public record BookingDto(
    long id,
    long userId,
    LocationDto fromLocation,
    LocationDto toLocation,
    String status,
    Long taxiId,
    long createdOn
) {

}
