package com.andreine.taxifleet.persistence.repository;

import java.util.List;

import com.andreine.taxifleet.persistence.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Order repository.
 */
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * Finds available orders.
     *
     * @return available orders
     */
    @Query(value = "SELECT o FROM OrderEntity o WHERE o.status = 'AVAILABLE'")
    List<OrderEntity> findAvailable();

    /**
     * Finds history of orders for the taxi.
     *
     * @param taxiId taxi id
     * @return list of orders
     */
    @Query(value = "SELECT o FROM OrderEntity o WHERE o.taxiId = :taxiId")
    List<OrderEntity> findHistory(long taxiId);

}