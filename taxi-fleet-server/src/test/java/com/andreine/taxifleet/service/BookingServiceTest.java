package com.andreine.taxifleet.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.andreine.taxifleet.integration.kafka.producer.BookingMessageProducer;
import com.andreine.taxifleet.model.MonthlyBookingStats;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.service.model.Booking;
import com.andreine.taxifleet.service.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TaxiRepository taxiRepository;

    @Mock
    private BookingMessageProducer bookingMessageProducer;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldGetAvailableBookings() {
        var bookings = List.of(
            bookingEntity()
                .id(1L)
                .build(),
            bookingEntity()
                .id(2L)
                .build());

        when(bookingRepository.findAvailable()).thenReturn(bookings);

        var availableBookings = bookingService.getAvailableBookings();

        assertThat(availableBookings).hasSize(2);
        assertThat(availableBookings.getFirst().id()).isEqualTo(1L);
        assertThat(availableBookings.getLast().id()).isEqualTo(2L);
    }

    @Test
    void shouldGetTaxiBookings() {
        var bookings = List.of(
            bookingEntity()
                .id(1L)
                .build(),
            bookingEntity()
                .id(2L)
                .build());

        when(bookingRepository.findByTaxi(1L)).thenReturn(bookings);

        var taxiBookings = bookingService.getTaxiBookings(1L);

        assertThat(taxiBookings).hasSize(2);
        assertThat(taxiBookings.getFirst().id()).isEqualTo(1L);
        assertThat(taxiBookings.getLast().id()).isEqualTo(2L);
    }

    @Test
    void shouldGetMonthlyBookingStats() {
        var monthlyBookingStats = List.of(
            MonthlyBookingStats.builder()
                .year(2025)
                .month(1)
                .totalBookings(200)
                .completedBookings(150)
                .cancelledBookings(20)
                .build());

        when(bookingRepository.getMonthlyStats()).thenReturn(monthlyBookingStats);

        assertThat(bookingService.getMonthlyBookingStats())
            .isEqualTo(monthlyBookingStats);
    }

    @Test
    void shouldRegisterBooking() {
        var booking = booking()
            .id(1L)
            .build();
        var bookingEntity = bookingEntity()
            .id(1L)
            .build();
        var taxi1 = TaxiEntity.builder()
            .id(1L)
            .build();
        var taxi2 = TaxiEntity.builder()
            .id(2L)
            .build();

        when(taxiRepository.findAvailable()).thenReturn(List.of(taxi1, taxi2));

        bookingService.registerBooking(booking);

        verify(bookingRepository).save(bookingEntity);
        verify(bookingMessageProducer).publishBookingMessage(booking, 1L);
        verify(bookingMessageProducer).publishBookingMessage(booking, 2L);
    }

    private BookingEntity.BookingEntityBuilder bookingEntity() {
        return BookingEntity.builder()
            .userId(1L)
            .status(BookingStatus.AVAILABLE)
            .originLatitude(2.0)
            .originLongitude(2.0)
            .destinationLatitude(1.0)
            .destinationLongitude(1.0)
            .createdOn(Instant.now().minus(10L, ChronoUnit.MINUTES));
    }

    private Booking.BookingBuilder booking() {
        return Booking.builder()
            .userId(1L)
            .status(Booking.BookingStatus.AVAILABLE)
            .fromLocation(new Location(2.0, 2.0))
            .toLocation(new Location(1.0, 1.0))
            .createdOnTs(100500L);
    }

}
