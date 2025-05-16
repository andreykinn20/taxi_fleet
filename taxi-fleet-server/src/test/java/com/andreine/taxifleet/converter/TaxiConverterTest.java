package com.andreine.taxifleet.converter;

import com.andreine.taxifleet.service.model.Booking;
import com.andreine.taxifleet.service.model.Location;
import com.andreine.taxifleet.service.model.Taxi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaxiConverterTest {

    @Test
    void convertConvertDomainToDto() {
        var taxi = Taxi.builder()
            .id(1L)
            .name("Taxi 1")
            .location(Location.of(10.0, 20.0))
            .status(Taxi.TaxiStatus.AVAILABLE)
            .registeredOn(100500L)
            .build();

        var taxiDto = TaxiConverter.convert(taxi);

        assertThat(taxiDto.id()).isEqualTo(1L);
        assertThat(taxiDto.name()).isEqualTo("Taxi 1");
        assertThat(taxiDto.location().latitude()).isEqualTo(10.0);
        assertThat(taxiDto.location().longitude()).isEqualTo(20.0);
        assertThat(taxiDto.status()).isEqualTo("AVAILABLE");
        assertThat(taxiDto.registeredOnSeconds()).isEqualTo(100500L);
    }

}
