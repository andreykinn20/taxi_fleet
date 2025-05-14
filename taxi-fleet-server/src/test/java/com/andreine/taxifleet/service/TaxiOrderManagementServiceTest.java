package com.andreine.taxifleet.service;

import java.util.Optional;

import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.model.OrderStatus;
import com.andreine.taxifleet.persistence.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiOrderManagementServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TaxiOrderManagementService taxiOrderManagementService;

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

}
