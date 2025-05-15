package com.andreine.taxifleet.service.model;

import lombok.Builder;

/**
 * Order.
 */
@Builder
public record Order(
    long id,
    long userId,
    Location fromLocation,
    Location toLocation,
    Long taxiId,
    OrderStatus status,
    long createdOnTs,
    Long updatedOnTs
) {

    public enum OrderStatus {
        AVAILABLE,
        BOOKED,
        FINISHED
    }

}
