package com.andreine.taxifleet.converter;

import com.andreine.taxifleet.controller.common.model.LocationDto;
import com.andreine.taxifleet.service.model.Location;
import lombok.experimental.UtilityClass;

/**
 * Location converter.
 */
@UtilityClass
public class LocationConverter {

    /**
     * Converts location to location dto.
     *
     * @param location location
     * @return location dto
     */
    public static LocationDto convert(Location location) {
        return LocationDto.builder()
            .latitude(location.latitude())
            .longitude(location.longitude())
            .build();
    }

}
