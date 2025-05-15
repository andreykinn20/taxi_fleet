package com.andreine.taxifleet.controller.publicapi.model;

import lombok.Builder;

/**
 * Order DTO.
 */
@Builder
public record OrderDto(
    long id,
    long userId,
    LocationDto fromLocation,
    LocationDto toLocation,
    String status,
    long createdOn
) {

}
