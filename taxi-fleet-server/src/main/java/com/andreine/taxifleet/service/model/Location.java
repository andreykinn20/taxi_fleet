package com.andreine.taxifleet.service.model;

/**
 * Location.
 */
public record Location(
    double latitude,
    double longitude
) {

    /**
     * Creates location.
     *
     * @param latitude  latitude
     * @param longitude longitude
     * @return location
     */
    public static Location of(double latitude, double longitude) {
        return new Location(latitude, longitude);
    }

}
