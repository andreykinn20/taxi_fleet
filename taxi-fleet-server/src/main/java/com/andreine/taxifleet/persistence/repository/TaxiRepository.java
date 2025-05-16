package com.andreine.taxifleet.persistence.repository;

import java.util.List;

import com.andreine.taxifleet.persistence.model.BookingEntity;
import com.andreine.taxifleet.persistence.model.TaxiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Taxi repository.
 */
public interface TaxiRepository extends JpaRepository<TaxiEntity, Long> {

    /**
     * Finds available taxis.
     *
     * @return available taxis
     */
    @Query(value = "SELECT t FROM TaxiEntity t WHERE t.status = 'AVAILABLE'")
    List<TaxiEntity> findAvailable();

}
