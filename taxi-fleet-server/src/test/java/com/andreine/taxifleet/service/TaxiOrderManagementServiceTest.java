package com.andreine.taxifleet.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.model.OrderStatus;
import com.andreine.taxifleet.persistence.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiOrderManagementServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TaxiOrderManagementService taxiOrderManagementService;

    @Test
    void shouldGetAvailableOrders() {
        var orders = List.of(
            orderEntity()
                .id(1L)
                .build(),
            orderEntity()
                .id(2L)
                .build());

        when(orderRepository.findAvailable()).thenReturn(orders);

        var availableOrders = taxiOrderManagementService.getAvailableOrders();

        assertThat(availableOrders).hasSize(2);
        assertThat(availableOrders.get(0).id()).isEqualTo(1L);
        assertThat(availableOrders.get(1).id()).isEqualTo(2L);
    }

    @Test
    void shouldBookOrder() {
        var order = OrderEntity.builder()
            .id(1L)
            .status(OrderStatus.AVAILABLE)
            .build();
        var bookedOrder = OrderEntity.builder()
            .id(1L)
            .taxiId(1L)
            .status(OrderStatus.BOOKED)
            .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(bookedOrder)).thenReturn(bookedOrder);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        taxiOrderManagementService.bookOrder(1L, 1L);

        verify(orderRepository).save(bookedOrder);
    }

    @Test
    void shouldGetOrdersHistory() {
        var orders = List.of(
            orderEntity()
                .id(1L)
                .build(),
            orderEntity()
                .id(2L)
                .build());

        when(orderRepository.findHistory(1L)).thenReturn(orders);

        var ordersHistory = taxiOrderManagementService.getOrdersHistory(1L);

        assertThat(ordersHistory).hasSize(2);
        assertThat(ordersHistory.getFirst().id()).isEqualTo(1L);
        assertThat(ordersHistory.get(1).id()).isEqualTo(2L);
    }

    private OrderEntity.OrderEntityBuilder orderEntity() {
        return OrderEntity.builder()
            .userId(1L)
            .status(OrderStatus.AVAILABLE)
            .originLatitude(2.0)
            .originLongitude(2.0)
            .destinationLatitude(1.0)
            .destinationLongitude(1.0)
            .createdOn(Instant.now().minus(10L, ChronoUnit.MINUTES));
    }

}
