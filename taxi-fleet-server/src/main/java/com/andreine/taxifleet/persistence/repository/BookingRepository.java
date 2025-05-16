package com.andreine.taxifleet.persistence.repository;

import java.util.List;

import com.andreine.taxifleet.persistence.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Booking repository.
 */
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    /**
     * Finds available bookings.
     *
     * @return available bookings
     */
    @Query(value = "SELECT o FROM BookingEntity o WHERE o.status = 'AVAILABLE'")
    List<BookingEntity> findAvailable();

    /**
     * Finds bookings for the taxi.
     *
     * @param taxiId taxi id
     * @return list of bookings
     */
    @Query(value = "SELECT o FROM BookingEntity o WHERE o.taxiId = :taxiId")
    List<BookingEntity> findByTaxi(long taxiId);

}