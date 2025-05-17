package com.andreine.taxifleet.persistence.repository;

import java.util.List;

import com.andreine.taxifleet.model.MonthlyBookingStats;
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

    /**
     * Gets monthly booking statistics.
     *
     * @return monthly booking statistics
     */
    @Query("""
            SELECT NEW com.andreine.taxifleet.model.MonthlyBookingStats(
                EXTRACT(YEAR FROM b.createdOn),
                EXTRACT(MONTH FROM b.createdOn),
                COUNT(b),
                SUM(CASE WHEN b.status = 'COMPLETED' THEN 1 ELSE 0 END),
                SUM(CASE WHEN b.status = 'CANCELLED' THEN 1 ELSE 0 END)
            )
            FROM BookingEntity b
            GROUP BY EXTRACT(YEAR FROM b.createdOn), EXTRACT(MONTH FROM b.createdOn)
            ORDER BY EXTRACT(YEAR FROM b.createdOn), EXTRACT(MONTH FROM b.createdOn)
        """)
    List<MonthlyBookingStats> getMonthlyStats();

}