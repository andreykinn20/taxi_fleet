package com.andreine.taxifleet.persistence.repository;

import com.andreine.taxifleet.persistence.model.TaxiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Taxi repository.
 */
public interface TaxiRepository extends JpaRepository<TaxiEntity, Long> {

}
