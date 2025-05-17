package com.andreine.taxifleet.controller.publicapi.model;

import com.andreine.taxifleet.controller.common.model.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Booking request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NonNull
    private Long userId;

    @NonNull
    private LocationDto fromLocation;

    @NonNull
    private LocationDto toLocation;

}