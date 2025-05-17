package com.andreine.taxifleet.controller.publicapi;

import java.util.List;

import com.andreine.taxifleet.controller.publicapi.model.BookingDto;
import com.andreine.taxifleet.controller.publicapi.model.BookingRequest;
import com.andreine.taxifleet.converter.BookingConverter;
import com.andreine.taxifleet.service.BookingService;
import com.andreine.taxifleet.service.TaxiBookingManagementService;
import com.andreine.taxifleet.service.TaxiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public api controller.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicApiController {

    private final TaxiBookingManagementService taxiBookingManagementService;

    private final BookingService bookingService;

    private final TaxiService taxiService;

    /**
     * POST /taxi/{taxiId}/bookings/{bookingId}/accept: accepts booking by the taxi.
     *
     * @param taxiId    (required)
     * @param bookingId (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/taxi/{taxiId}/bookings/{bookingId}/accept")
    public ResponseEntity<Void> acceptBooking(@PathVariable Long taxiId, @PathVariable Long bookingId) {
        taxiBookingManagementService.acceptBooking(taxiId, bookingId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /public/taxi/{taxiId}/bookings": Gets bookings for the taxi.
     *
     * @param taxiId taxi id (required)
     * @return OK (status code 200)
     */
    @GetMapping("/public/taxi/{taxiId}/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(@PathVariable Long taxiId) {
        var taxiBookings = bookingService.getTaxiBookings(taxiId).stream()
            .map(BookingConverter::toDto)
            .toList();

        return ResponseEntity.ok(taxiBookings);
    }

    /**
     * GET /public/bookings/available: Gets available bookings.
     *
     * @return OK (status code 200)
     */
    @GetMapping("/public/bookings/available")
    public ResponseEntity<List<BookingDto>> getAvailableBookings() {
        var availableBookings = bookingService.getAvailableBookings().stream()
            .map(BookingConverter::toDto)
            .toList();

        return ResponseEntity.ok(availableBookings);
    }

    /**
     * POST /taxi/{taxiId}/status/available: sets taxi status to available.
     *
     * @param taxiId taxi id (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/taxi/{taxiId}/status/unavailable")
    public ResponseEntity<Void> setTaxiUnavailable(@PathVariable Long taxiId) {
        taxiService.setTaxiUnavailable(taxiId);

        return ResponseEntity.noContent().build();
    }

    /**
     * POST /taxi/{taxiId}/status/available: sets taxi status to available.
     *
     * @param taxiId taxi id (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/taxi/{taxiId}/status/available")
    public ResponseEntity<Void> setTaxiAvailable(@PathVariable Long taxiId) {
        taxiService.setTaxiAvailable(taxiId);

        return ResponseEntity.noContent().build();
    }

    /**
     * POST /taxi/{taxiId}/status/available: completes booking.
     *
     * @param taxiId    taxi id (required)
     * @param bookingId booking id (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/taxi/{taxiId}/bookings/{bookingId}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable Long taxiId, @PathVariable Long bookingId) {
        taxiBookingManagementService.completeBooking(taxiId, bookingId);

        return ResponseEntity.noContent().build();
    }

    /**
     * POST /public/bookings: registers booking.
     *
     * @param bookingRequest booking request (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/bookings")
    public ResponseEntity<Void> registerBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.registerBooking(BookingConverter.fromRequest(bookingRequest));

        return ResponseEntity.noContent().build();
    }


    /**
     * POST /public/bookings/{bookingId}/cancel: cancels booking.
     *
     * @param bookingRequest booking request (required)
     * @return NO CONTENT (status code 204)
     */
    @PostMapping("/public/bookings/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.registerBooking(BookingConverter.fromRequest(bookingRequest));

        return ResponseEntity.noContent().build();
    }

}