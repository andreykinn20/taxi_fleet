package com.andreine.taxifleet.service;

import java.time.Instant;
import java.util.List;

import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.service.model.Taxi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiServiceTest {

    @Mock
    private TaxiRepository taxiRepository;

    @InjectMocks
    private TaxiService taxiService;

    @Test
    void shouldGetTaxis() {
        when(taxiRepository.findAll()).thenReturn(List.of(
            taxiEntity()
                .id(1L)
                .name("Taxi 1")
                .build(),
            taxiEntity()
                .id(2L)
                .name("Taxi 2")
                .build()));

        var taxis = taxiService.getTaxis();

        assertThat(taxis).hasSize(2);
        assertThat(taxis.getFirst().id()).isEqualTo(1L);
        assertThat(taxis.getFirst().name()).isEqualTo("Taxi 1");
        assertThat(taxis.getFirst().location().latitude()).isEqualTo(20.0);
        assertThat(taxis.getFirst().location().longitude()).isEqualTo(10.0);
        assertThat(taxis.getFirst().status()).isEqualTo(Taxi.TaxiStatus.AVAILABLE);
        assertThat(taxis.getFirst().registeredOn()).isEqualTo(100500L);
        assertThat(taxis.getLast().id()).isEqualTo(2L);
        assertThat(taxis.getLast().name()).isEqualTo("Taxi 2");
        assertThat(taxis.getLast().location().latitude()).isEqualTo(20.0);
        assertThat(taxis.getLast().location().longitude()).isEqualTo(10.0);
        assertThat(taxis.getLast().status()).isEqualTo(Taxi.TaxiStatus.AVAILABLE);
        assertThat(taxis.getLast().registeredOn()).isEqualTo(100500L);
    }

    private TaxiEntity.TaxiEntityBuilder taxiEntity() {
        return TaxiEntity.builder()
            .name("Taxi")
            .longitude(10.0)
            .latitude(20.0)
            .registeredOn(Instant.ofEpochMilli(100500))
            .status(TaxiStatus.AVAILABLE);
    }

}
