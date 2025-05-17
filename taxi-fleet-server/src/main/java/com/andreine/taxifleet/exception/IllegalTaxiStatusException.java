package com.andreine.taxifleet.exception;

/**
 * Exception is thrown when taxi has illegal status.
 */
public class IllegalTaxiStatusException extends RuntimeException {

    public IllegalTaxiStatusException(String message) {
        super(message);
    }

}
