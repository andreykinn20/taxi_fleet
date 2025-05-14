package com.andreine.taxifleet.exception;

public class OrderNotAvailableException extends RuntimeException {

    public OrderNotAvailableException(Long orderId) {
        super("Order with ID %s is not available for booking".formatted(orderId));
    }

}
