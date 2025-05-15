package com.andreine.taxifleet.controller.publicapi;

import java.util.List;

import com.andreine.taxifleet.controller.publicapi.model.OrderDto;
import com.andreine.taxifleet.converter.OrderConverter;
import com.andreine.taxifleet.service.TaxiOrderManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Taxi controller.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaxiController {

    private final TaxiOrderManagementService taxiOrderManagementService;

    /**
     * GET /taxi/{taxiId}/orders/available: Gets orders available for booking.
     *
     * @return OK (status code 200)
     */
    @GetMapping("/public/taxi/{taxiId}/orders/available")
    public ResponseEntity<List<OrderDto>> getAvailableOrders() {
        var availableOrders = taxiOrderManagementService.getAvailableOrders().stream()
            .map(OrderConverter::convert)
            .toList();

        return ResponseEntity.ok(availableOrders);
    }

    /**
     * POST /taxi/{taxiId}/orders/{orderId}/book: books order by the taxi.
     *
     * @param taxiId  (required)
     * @param orderId (required)
     * @return OK (status code 200)
     */
    @PostMapping("/public/taxi/{taxiId}/orders/{orderId}/book")
    public ResponseEntity<Void> bookOrder(@PathVariable Long taxiId, @PathVariable Long orderId) {
        taxiOrderManagementService.bookOrder(taxiId, orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /taxi/{taxiId}/orders/history: Gets orders history for the taxi.
     *
     * @param taxiId taxi id (required)
     * @return OK (status code 200)
     */
    @GetMapping("/public/taxi/{taxiId}/orders/history")
    public ResponseEntity<List<OrderDto>> getOrdersHistory(@PathVariable Long taxiId) {
        var taxiOrdersHistory = taxiOrderManagementService.getOrdersHistory(taxiId).stream()
            .map(OrderConverter::convert)
            .toList();

        return ResponseEntity.ok(taxiOrdersHistory);
    }

}