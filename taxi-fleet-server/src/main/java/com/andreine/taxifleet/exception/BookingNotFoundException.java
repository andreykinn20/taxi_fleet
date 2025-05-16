package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when booking is not found.
 */
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long bookingId) {
        super("Booking %s is not found".formatted(bookingId));
    }

}
