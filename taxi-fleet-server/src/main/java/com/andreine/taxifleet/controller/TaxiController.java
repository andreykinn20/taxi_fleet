package com.andreine.taxifleet.controller;

import java.util.List;

import com.andreine.taxifleet.controller.model.OrderDto;
import com.andreine.taxifleet.service.TaxiOrderManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaxiController {

    private final TaxiOrderManagementService taxiOrderManagementService;

    @GetMapping("/taxi/{taxiId}/orders/available")
    public ResponseEntity<List<OrderDto>> getAvailableOrders() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/taxi/{taxiId}/orders/{orderId}/book")
    public ResponseEntity<Void> bookOrder(@PathVariable Long taxiId, @PathVariable Long orderId) {
        taxiOrderManagementService.bookOrder(taxiId, orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/taxi/{taxiId}/orders/history")
    public ResponseEntity<List<OrderDto>> getOrdersHistory() {
        return ResponseEntity.ok(List.of());
    }

}