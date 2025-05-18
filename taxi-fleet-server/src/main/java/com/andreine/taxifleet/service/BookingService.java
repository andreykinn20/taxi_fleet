package com.andreine.taxifleet.service;

import java.util.List;

import com.andreine.taxifleet.converter.BookingConverter;
import com.andreine.taxifleet.exception.BookingNotFoundException;
import com.andreine.taxifleet.exception.IllegalBookingStatusException;
import com.andreine.taxifleet.integration.kafka.producer.BookingMessageProducer;
import com.andreine.taxifleet.model.MonthlyBookingStats;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.service.model.Booking;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Booking service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    private final TaxiRepository taxiRepository;

    private final BookingMessageProducer bookingMessageProducer;

    /**
     * Registers the booking.
     *
     * @param booking booking
     */
    public void registerBooking(Booking booking) {
        BookingEntity saved = bookingRepository.save(BookingConverter.toEntity(booking));
        Booking convertedBooking = BookingConverter.fromEntity(saved);

        taxiRepository.findAvailable().stream()
            .map(TaxiEntity::getId)
            .forEach(taxiId -> bookingMessageProducer.publishBookingMessage(convertedBooking, taxiId));

        log.info("Booking {} was successfully registered", convertedBooking.id());
    }

    /**
     * Cancels the booking.
     *
     * @param bookingId booking id
     */
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public void cancelBooking(long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(bookingId));

        BookingStatus bookingStatus = booking.getStatus();
        if (BookingStatus.AVAILABLE != bookingStatus && BookingStatus.ACCEPTED != bookingStatus) {
            throw new IllegalBookingStatusException("Booking %s is not allowed to be cancelled".formatted(bookingId));
        }

        Long taxiId = booking.getTaxiId();
        bookingRepository.save(booking.toBuilder()
            .status(BookingStatus.CANCELLED)
            .taxiId(null)
            .build());

        if (BookingStatus.ACCEPTED == bookingStatus) {
            var taxi = taxiRepository.findById(taxiId);
            taxi.ifPresent(taxiEntity -> taxiRepository.save(taxiEntity.toBuilder()
                .status(TaxiStatus.AVAILABLE)
                .build()));
        }

        log.info("Booking {} was cancelled, taxi {}", bookingId, taxiId);
    }

    /**
     * Gets available bookings.
     *
     * @return list of available bookings
     */
    public List<Booking> getAvailableBookings() {
        return bookingRepository.findAvailable().stream()
            .map(BookingConverter::fromEntity)
            .toList();
    }

    /**
     * Gets bookings for the taxi.
     *
     * @param taxiId taxi id
     * @return bookings for the taxi
     */
    public List<Booking> getTaxiBookings(long taxiId) {
        return bookingRepository.findByTaxi(taxiId).stream()
            .map(BookingConverter::fromEntity)
            .toList();
    }

    /**
     * Gets monthly bookings statistics.
     *
     * @return monthly bookings statistics
     */
    public List<MonthlyBookingStats> getMonthlyBookingStats() {
        return bookingRepository.getMonthlyStats();
    }

}
