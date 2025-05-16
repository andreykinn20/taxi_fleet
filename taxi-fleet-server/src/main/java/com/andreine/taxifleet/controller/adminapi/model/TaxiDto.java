package com.andreine.taxifleet.controller.adminapi.model;

import com.andreine.taxifleet.controller.common.model.LocationDto;
import lombok.Builder;

/**
 * Taxi DTO.
 */
@Builder
public record TaxiDto(
    long id,
    String name,
    LocationDto location,
    String status,
    long registeredOnSeconds
) {

}
