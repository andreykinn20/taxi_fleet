package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when taxi is not available.
 */
public class TaxiNotAvailableException extends RuntimeException {

    public TaxiNotAvailableException(Long taxiId) {
        super("Taxi with ID %s is not available for booking".formatted(taxiId));
    }

}
