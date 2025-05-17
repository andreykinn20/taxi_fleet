package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when booking has illegal status.
 */
public class IllegalBookingStatusException extends RuntimeException {

    public IllegalBookingStatusException(String message) {
        super(message);
    }

}
