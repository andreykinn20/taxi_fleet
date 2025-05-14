package com.andreine.taxifleet.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order with ID %s is not found".formatted(orderId));
    }

}
