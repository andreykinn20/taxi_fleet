package com.andreine.taxifleet.persistence.repository;

import com.andreine.taxifleet.persistence.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}