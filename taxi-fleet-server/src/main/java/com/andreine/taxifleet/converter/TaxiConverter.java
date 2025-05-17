package com.andreine.taxifleet.converter;

import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
import com.andreine.taxifleet.controller.common.model.LocationDto;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.service.model.Location;
import com.andreine.taxifleet.service.model.Taxi;

/**
 * Taxi converter.
 */
public class TaxiConverter {

    /**
     * Converts taxi entity to domain.
     *
     * @param taxiEntity taxi entity
     * @return taxi
     */
    public static Taxi fromEntity(TaxiEntity taxiEntity) {
        return Taxi.builder()
            .id(taxiEntity.getId())
            .name(taxiEntity.getName())
            .location(taxiEntity.hasLocation()
                ? new Location(taxiEntity.getLatitude(), taxiEntity.getLongitude())
                : null)
            .status(Taxi.TaxiStatus.valueOf(taxiEntity.getStatus().name()))
            .registeredOn(taxiEntity.getRegisteredOn().toEpochMilli())
            .build();
    }

    /**
     * Converts taxi to taxi dto.
     *
     * @param taxi taxi
     * @return taxi dto
     */
    public static TaxiDto toDto(Taxi taxi) {
        return TaxiDto.builder()
            .id(taxi.id())
            .name(taxi.name())
            .location(taxi.location() != null
                ? new LocationDto(taxi.location().latitude(), taxi.location().longitude())
                : null)
            .status(taxi.status().name())
            .registeredOnSeconds(TimeUnit.MILLISECONDS.toSeconds(taxi.registeredOn()))
            .build();
    }

}
