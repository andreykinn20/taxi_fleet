package com.andreine.taxifleet.controller.publicapi.model;

import com.andreine.taxifleet.controller.common.model.LocationDto;
import lombok.Builder;
import lombok.NonNull;

/**
 * Booking request.
 */
@Builder
public record BookingRequest(
    @NonNull
    Long userId,
    @NonNull
    LocationDto fromLocation,
    @NonNull
    LocationDto toLocation
) {

}
