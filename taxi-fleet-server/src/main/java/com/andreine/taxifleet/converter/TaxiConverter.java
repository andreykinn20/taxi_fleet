package com.andreine.taxifleet.converter;

import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
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
    public static Taxi convert(TaxiEntity taxiEntity) {
        return Taxi.builder()
            .id(taxiEntity.getId())
            .name(taxiEntity.getName())
            .location(Location.of(taxiEntity.getLatitude(), taxiEntity.getLongitude()))
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
    public static TaxiDto convert(Taxi taxi) {
        return TaxiDto.builder()
            .id(taxi.id())
            .name(taxi.name())
            .location(LocationConverter.convert(taxi.location()))
            .status(taxi.status().name())
            .registeredOnSeconds(TimeUnit.MILLISECONDS.toSeconds(taxi.registeredOn()))
            .build();
    }

}
