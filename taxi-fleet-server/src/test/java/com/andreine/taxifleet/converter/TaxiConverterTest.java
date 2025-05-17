package com.andreine.taxifleet.converter;

import java.time.Instant;

import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.service.model.Location;
import com.andreine.taxifleet.service.model.Taxi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaxiConverterTest {

    @Test
    void shouldConvertEntityToDomain() {
        var taxiEntity = TaxiEntity.builder()
            .id(1L)
            .status(TaxiStatus.AVAILABLE)
            .longitude(10.0)
            .latitude(20.0)
            .name("Taxi")
            .registeredOn(Instant.ofEpochMilli(100500L))
            .build();

        var taxi = TaxiConverter.fromEntity(taxiEntity);

        assertThat(taxi.id()).isEqualTo(1L);
        assertThat(taxi.name()).isEqualTo("Taxi");
        assertThat(taxi.location().latitude()).isEqualTo(20.0);
        assertThat(taxi.location().longitude()).isEqualTo(10.0);
        assertThat(taxi.status()).isEqualTo(Taxi.TaxiStatus.AVAILABLE);
        assertThat(taxi.registeredOn()).isEqualTo(100500L);
    }

    @Test
    void convertConvertDomainToDto() {
        var taxi = Taxi.builder()
            .id(1L)
            .name("Taxi 1")
            .location(new Location(10.0, 20.0))
            .status(Taxi.TaxiStatus.AVAILABLE)
            .registeredOn(100500L)
            .build();

        var taxiDto = TaxiConverter.toDto(taxi);

        assertThat(taxiDto.id()).isEqualTo(1L);
        assertThat(taxiDto.name()).isEqualTo("Taxi 1");
        assertThat(taxiDto.location().getLatitude()).isEqualTo(10.0);
        assertThat(taxiDto.location().getLongitude()).isEqualTo(20.0);
        assertThat(taxiDto.status()).isEqualTo("AVAILABLE");
        assertThat(taxiDto.registeredOnSeconds()).isEqualTo(100L);
    }

}
