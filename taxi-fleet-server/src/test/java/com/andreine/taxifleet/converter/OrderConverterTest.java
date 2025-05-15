package com.andreine.taxifleet.converter;

import java.time.Instant;

import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.persistence.model.OrderStatus;
import com.andreine.taxifleet.service.model.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderConverterTest {

    @Test
    void shouldConvertEntityToDomain() {
        var orderEntity = OrderEntity.builder()
            .id(1L)
            .userId(2L)
            .originLongitude(10.0)
            .originLatitude(20.0)
            .destinationLongitude(30.0)
            .destinationLatitude(40.0)
            .status(OrderStatus.AVAILABLE)
            .taxiId(1L)
            .createdOn(Instant.ofEpochMilli(1000L))
            .updatedOn(Instant.ofEpochMilli(2000L))
            .build();

        var order = OrderConverter.convert(orderEntity);

        assertThat(order.id()).isEqualTo(1L);
        assertThat(order.userId()).isEqualTo(2L);
        assertThat(order.fromLocation().latitude()).isEqualTo(20.0);
        assertThat(order.fromLocation().longitude()).isEqualTo(10.0);
        assertThat(order.toLocation().latitude()).isEqualTo(40.0);
        assertThat(order.toLocation().longitude()).isEqualTo(30.0);
        assertThat(order.toLocation().latitude()).isEqualTo(40.0);
        assertThat(order.status()).isEqualTo(Order.OrderStatus.AVAILABLE);
        assertThat(order.taxiId()).isEqualTo(1L);
        assertThat(order.createdOnTs()).isEqualTo(1000L);
        assertThat(order.updatedOnTs()).isEqualTo(2000L);
    }

    @Test
    void convertConvertEntityToDomainWithNullUpdatedOn() {
        var orderEntity = OrderEntity.builder()
            .id(1L)
            .userId(2L)
            .originLongitude(10.0)
            .originLatitude(20.0)
            .destinationLongitude(30.0)
            .destinationLatitude(40.0)
            .status(OrderStatus.AVAILABLE)
            .taxiId(1L)
            .createdOn(Instant.ofEpochMilli(1000L))
            .build();

        var order = OrderConverter.convert(orderEntity);

        assertThat(order.updatedOnTs()).isNull();
    }

}
