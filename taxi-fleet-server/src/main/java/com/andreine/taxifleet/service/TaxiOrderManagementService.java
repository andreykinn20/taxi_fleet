package com.andreine.taxifleet.service;

import java.util.List;

import com.andreine.taxifleet.converter.OrderConverter;
import com.andreine.taxifleet.exception.OrderNotAvailableException;
import com.andreine.taxifleet.exception.OrderNotFoundException;
import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.repository.OrderRepository;
import com.andreine.taxifleet.service.model.Order;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.andreine.taxifleet.persistence.model.OrderStatus.AVAILABLE;
import static com.andreine.taxifleet.persistence.model.OrderStatus.BOOKED;

/**
 * Service for managing taxi orders.
 */
@Service
@RequiredArgsConstructor
public class TaxiOrderManagementService {

    private final OrderRepository orderRepository;

    /**
     * Gets orders available for booking.
     *
     * @return list of available orders
     */
    public List<Order> getAvailableOrders() {
        return orderRepository.findAvailable().stream()
            .map(OrderConverter::convert)
            .toList();
    }

    /**
     * Books order for the taxi.
     *
     * @param taxiId  taxi id
     * @param orderId order id
     */
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
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

    /**
     * Gets orders history for the taxi.
     *
     * @param taxiId taxi id
     * @return orders history for taxi
     */
    public List<Order> getOrdersHistory(long taxiId) {
        return orderRepository.findHistory(taxiId).stream()
            .map(OrderConverter::convert)
            .toList();
    }

}
