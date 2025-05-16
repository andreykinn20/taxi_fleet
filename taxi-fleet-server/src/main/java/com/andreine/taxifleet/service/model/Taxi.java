package com.andreine.taxifleet.service.model;

import lombok.Builder;

/**
 * Taxi.
 */
@Builder
public record Taxi(
    long id,
    String name,
    TaxiStatus status,
    Location location,
    long registeredOn
) {

    /**
     * Taxi status.
     */
    public enum TaxiStatus {
        AVAILABLE,
        BOOKED,
        UNAVAILABLE
    }

}
