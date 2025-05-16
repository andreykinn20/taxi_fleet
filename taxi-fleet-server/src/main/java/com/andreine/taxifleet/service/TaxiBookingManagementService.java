package com.andreine.taxifleet.service;

import com.andreine.taxifleet.exception.BookingNotFoundException;
import com.andreine.taxifleet.exception.IllegalBookingStatusException;
import com.andreine.taxifleet.exception.IllegalTaxiStatusException;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.BookingStatus;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Service for managing taxi bookings.
 */
@Service
@RequiredArgsConstructor
public class TaxiBookingManagementService {

    private final BookingRepository bookingRepository;

    private final TaxiRepository taxiRepository;

    /**
     * Accepts booking for the taxi.
     *
     * @param taxiId    taxi id
     * @param bookingId booking id
     */
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public void acceptBooking(Long taxiId, Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(bookingId));

        TaxiEntity taxi = taxiRepository.findById(taxiId)
            .orElseThrow(() -> new TaxiNotFoundException(bookingId));

        if (!BookingStatus.AVAILABLE.equals(booking.getStatus())) {
            throw new IllegalBookingStatusException("Booking %s is not available".formatted(bookingId));
        }

        if (!TaxiStatus.AVAILABLE.equals(taxi.getStatus())) {
            throw new IllegalTaxiStatusException("Taxi %s is not available".formatted(taxiId));
        }

        bookingRepository.save(booking.toBuilder()
            .taxiId(taxiId)
            .status(BookingStatus.ACCEPTED)
            .build());

        taxiRepository.save(taxi.toBuilder()
            .status(TaxiStatus.BOOKED)
            .build());
    }

    /**
     * Completes booking for the taxi.
     *
     * @param taxiId    taxi id
     * @param bookingId booking id
     */
    @Transactional
    public void completeBooking(Long taxiId, Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(bookingId));

        TaxiEntity taxi = taxiRepository.findById(taxiId)
            .orElseThrow(() -> new TaxiNotFoundException(bookingId));

        if (!BookingStatus.ACCEPTED.equals(booking.getStatus())) {
            throw new IllegalBookingStatusException("Booking %s is not in progress".formatted(bookingId));
        }

        if (!TaxiStatus.BOOKED.equals(taxi.getStatus())) {
            throw new IllegalTaxiStatusException("Taxi %s is not booked".formatted(taxiId));
        }

        bookingRepository.save(booking.toBuilder()
            .status(BookingStatus.COMPLETED)
            .build());

        taxiRepository.save(taxi.toBuilder()
            .status(TaxiStatus.AVAILABLE)
            .build());
    }

}
