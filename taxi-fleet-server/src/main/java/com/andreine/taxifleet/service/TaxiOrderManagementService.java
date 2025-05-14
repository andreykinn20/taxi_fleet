package com.andreine.taxifleet.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;

import com.andreine.taxifleet.exception.OrderNotAvailableException;
import com.andreine.taxifleet.exception.OrderNotFoundException;
import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.andreine.taxifleet.persistence.model.OrderStatus.AVAILABLE;
import static com.andreine.taxifleet.persistence.model.OrderStatus.BOOKED;

@Service
@RequiredArgsConstructor
public class TaxiOrderManagementService {

    private final OrderRepository orderRepository;

    @Transactional
    @Retryable(OptimisticLockException.class)
    public void bookOrder(Long taxiId, Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!AVAILABLE.equals(order.getStatus())) {
            throw new OrderNotAvailableException(orderId);
        }

        orderRepository.save(order.toBuilder()
            .taxiId(taxiId)
            .status(BOOKED)
            .build());
    }

}
