package com.andreine.taxifleet.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.andreine.taxifleet.exception.IllegalTaxiStatusException;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.service.model.Taxi;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
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

    @Test
    void shouldSetTaxiAvailable() {
        var taxi = taxiEntity()
            .id(1L)
            .status(TaxiStatus.UNAVAILABLE)
            .build();

        when(taxiRepository.findById(1L)).thenReturn(Optional.of(taxi));

        taxiService.setTaxiAvailable(1L);

        verify(taxiRepository).save(argThat(updatedTaxi -> {
            assertThat(updatedTaxi.getStatus()).isEqualTo(TaxiStatus.AVAILABLE);
            return true;
        }));
    }

    @Test
    void shouldThrowExceptionWhenSetTaxiAvailableIfTaxiIsNotUnavailable() {
        var taxi = taxiEntity()
            .id(1L)
            .status(TaxiStatus.BOOKED)
            .build();

        when(taxiRepository.findById(1L)).thenReturn(Optional.of(taxi));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> taxiService.setTaxiAvailable(1L);

        assertThatExceptionOfType(IllegalTaxiStatusException.class)
            .isThrownBy(throwingCallable)
            .withMessage("Taxi 1 is not unavailable");
    }

    @Test
    void shouldThrowExceptionWhenSetTaxiAvailableIfTaxiIsNotFound() {
        when(taxiRepository.findById(1L)).thenReturn(Optional.empty());

        ThrowableAssert.ThrowingCallable throwingCallable = () -> taxiService.setTaxiUnavailable(1L);

        assertThatExceptionOfType(TaxiNotFoundException.class)
            .isThrownBy(throwingCallable)
            .withMessage("Taxi 1 is not found");
    }

    @Test
    void shouldSetTaxiUnavailable() {
        var taxi = taxiEntity()
            .id(1L)
            .status(TaxiStatus.AVAILABLE)
            .build();

        when(taxiRepository.findById(1L)).thenReturn(Optional.of(taxi));

        taxiService.setTaxiUnavailable(1L);

        verify(taxiRepository).save(argThat(updatedTaxi -> {
            assertThat(updatedTaxi.getStatus()).isEqualTo(TaxiStatus.UNAVAILABLE);
            return true;
        }));
    }

    @Test
    void shouldThrowExceptionWhenSetTaxiUnavailableIfTaxiIsNotFound() {
        var taxi = taxiEntity()
            .id(1L)
            .status(TaxiStatus.BOOKED)
            .build();

        when(taxiRepository.findById(1L)).thenReturn(Optional.of(taxi));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> taxiService.setTaxiUnavailable(1L);

        assertThatExceptionOfType(IllegalTaxiStatusException.class)
            .isThrownBy(throwingCallable)
            .withMessage("Taxi 1 is not available");
    }

    @Test
    void shouldThrowExceptionWhenSetTaxiUnavailableIfTaxiIsNotAvailable() {
        when(taxiRepository.findById(1L)).thenReturn(Optional.empty());

        ThrowableAssert.ThrowingCallable throwingCallable = () -> taxiService.setTaxiUnavailable(1L);

        assertThatExceptionOfType(TaxiNotFoundException.class)
            .isThrownBy(throwingCallable)
            .withMessage("Taxi 1 is not found");
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
