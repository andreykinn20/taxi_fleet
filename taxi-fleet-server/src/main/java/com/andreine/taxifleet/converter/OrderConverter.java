package com.andreine.taxifleet.converter;

import java.util.concurrent.TimeUnit;

import com.andreine.taxifleet.controller.publicapi.model.OrderDto;
import com.andreine.taxifleet.persistence.model.OrderEntity;
import com.andreine.taxifleet.service.model.Location;
import com.andreine.taxifleet.service.model.Order;
import lombok.experimental.UtilityClass;

/**
 * Order converter.
 */
@UtilityClass
public class OrderConverter {

    /**
     * Converts order entity to order.
     *
     * @param orderEntity order entity
     * @return order
     */
    public static Order convert(OrderEntity orderEntity) {
        return Order.builder()
            .id(orderEntity.getId())
            .userId(orderEntity.getUserId())
            .fromLocation(Location.of(orderEntity.getOriginLatitude(), orderEntity.getOriginLongitude()))
            .toLocation(Location.of(orderEntity.getDestinationLatitude(), orderEntity.getDestinationLongitude()))
            .status(Order.OrderStatus.valueOf(orderEntity.getStatus().name()))
            .taxiId(orderEntity.getTaxiId())
            .createdOnTs(orderEntity.getCreatedOn().toEpochMilli())
            .updatedOnTs(orderEntity.getUpdatedOn() != null ? orderEntity.getUpdatedOn().toEpochMilli() : null)
            .build();
    }

    /**
     * Converts order to order dto.
     *
     * @param order order
     * @return order dto
     */
    public static OrderDto convert(Order order) {
        return OrderDto.builder()
            .id(order.id())
            .userId(order.userId())
            .fromLocation(LocationConverter.convert(order.fromLocation()))
            .toLocation(LocationConverter.convert(order.toLocation()))
            .status(order.status().name())
            .createdOn(TimeUnit.MILLISECONDS.toSeconds(order.createdOnTs()))
            .build();
    }

}
