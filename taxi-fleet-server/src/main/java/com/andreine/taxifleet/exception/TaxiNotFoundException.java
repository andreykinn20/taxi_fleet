package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when taxi is not found.
 */
public class TaxiNotFoundException extends RuntimeException {

    public TaxiNotFoundException(Long taxiId) {
        super("Taxi %s is not found".formatted(taxiId));
    }

}
