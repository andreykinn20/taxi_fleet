package com.andreine.taxifleet.converter;

import com.andreine.taxifleet.service.model.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationConverterTest {

    @Test
    void shouldConvert() {
        var location = new Location(1.0, 2.0);

        var locationDto = LocationConverter.convert(location);

        assertThat(locationDto.latitude()).isEqualTo(1.0);
        assertThat(locationDto.longitude()).isEqualTo(2.0);
    }

}
