package com.andreine.taxifleet.controller.adminapi;

import java.util.List;

import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
import com.andreine.taxifleet.converter.TaxiConverter;
import com.andreine.taxifleet.model.MonthlyBookingStats;
import com.andreine.taxifleet.service.BookingService;
import com.andreine.taxifleet.service.TaxiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin api controller.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AdminApiController {

    private final TaxiService taxiService;

    private final BookingService bookingService;

    /**
     * GET /public/taxis: Gets taxis.
     *
     * @return OK (status code 200)
     */
    @GetMapping("/admin/taxis")
    public ResponseEntity<List<TaxiDto>> getTaxis() {
        var taxis = taxiService.getTaxis().stream()
            .map(TaxiConverter::convert)
            .toList();

        return ResponseEntity.ok(taxis);
    }

    /**
     * GET /bookings-by-month: Gets bookings by month.
     *
     * @return OK (status code 200)
     */
    @GetMapping("/admin/bookings-by-month")
    public ResponseEntity<List<MonthlyBookingStats>> getMonthlyBookingStats() {
        var monthlyBookingStats = bookingService.getMonthlyBookingStats();

        return ResponseEntity.ok(monthlyBookingStats);
    }

}
