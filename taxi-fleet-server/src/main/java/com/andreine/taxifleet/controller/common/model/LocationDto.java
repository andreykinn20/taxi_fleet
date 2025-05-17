package com.andreine.taxifleet.controller.common.model;

import lombok.Builder;
import lombok.NonNull;

/**
 * Location DTO.
 */
@Builder
public record LocationDto(
    @NonNull
    Double latitude,
    @NonNull
    Double longitude
) {

}
