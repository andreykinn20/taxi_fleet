package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when booking is not available.
 */
public class BookingNotAvailableException extends RuntimeException {

    public BookingNotAvailableException(Long bookingId) {
        super("Booking with ID %s is not available".formatted(bookingId));
    }

}
