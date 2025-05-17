package com.andreine.taxifleet.model;

import lombok.Builder;

/**
 * Monthly booking stats.
 */
@Builder
public record MonthlyBookingStats(
    int year,
    int month,
    long totalBookings,
    long completedBookings,
    long cancelledBookings
) {

}
