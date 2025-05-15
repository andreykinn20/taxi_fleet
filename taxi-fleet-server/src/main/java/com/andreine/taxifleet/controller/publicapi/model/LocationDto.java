package com.andreine.taxifleet.controller.publicapi.model;

import lombok.Builder;

/**
 * Location DTO.
 */
@Builder
public record LocationDto(
    double latitude,
    double longitude
) {

}
