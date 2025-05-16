package com.andreine.taxifleet.service;

import java.time.Instant;
import java.util.Optional;

import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaxiBookingManagementServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TaxiRepository taxiRepository;

    @InjectMocks
    private TaxiBookingManagementService taxiBookingManagementService;

    @Test
    void shouldAcceptBooking() {
        var taxi = TaxiEntity.builder()
            .id(1L)
            .name("Taxi 1")
            .status(TaxiStatus.AVAILABLE)
            .registeredOn(Instant.ofEpochMilli(100500))
            .build();
        var booking = BookingEntity.builder()
            .id(1L)
            .status(BookingStatus.AVAILABLE)
            .build();
        var bookedTaxi = TaxiEntity.builder()
            .id(1L)
            .name("Taxi 1")
            .status(TaxiStatus.BOOKED)
            .registeredOn(Instant.ofEpochMilli(100500))
            .build();
        var acceptedBooking = BookingEntity.builder()
            .id(1L)
            .taxiId(1L)
            .status(BookingStatus.ACCEPTED)
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(taxiRepository.findById(1L)).thenReturn(Optional.of(taxi));

        taxiBookingManagementService.acceptBooking(1L, 1L);

        verify(bookingRepository).save(acceptedBooking);
        verify(taxiRepository).save(bookedTaxi);
    }

}
