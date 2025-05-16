package com.andreine.taxifleet.service;

import java.util.List;

import com.andreine.taxifleet.converter.BookingConverter;
import com.andreine.taxifleet.model.MonthlyBookingStats;
import com.andreine.taxifleet.persistence.repository.BookingRepository;
import com.andreine.taxifleet.service.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Booking service.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Gets available bookings.
     *
     * @return list of available bookings
     */
    public List<Booking> getAvailableBookings() {
        return bookingRepository.findAvailable().stream()
            .map(BookingConverter::convert)
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
            .map(BookingConverter::convert)
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
