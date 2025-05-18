package com.andreine.taxifleet.controller.publicapi;

import com.andreine.taxifleet.controller.BaseControllerTest;
import com.andreine.taxifleet.exception.BookingNotFoundException;
import com.andreine.taxifleet.exception.IllegalBookingStatusException;
import com.andreine.taxifleet.exception.IllegalTaxiStatusException;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicApiControllerTest extends BaseControllerTest {

    @Test
    @SneakyThrows
    void shouldHandleRuntimeException() {
        when(bookingService.getAvailableBookings()).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(get("/public/bookings/available"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message", containsString("Error occurred")));
    }

    @Test
    @SneakyThrows
    void shouldHandleTaxiNotFoundException() {
        doThrow(new TaxiNotFoundException(1L)).when(taxiBookingManagementService).acceptBooking(1L, 1L);

        mockMvc.perform(post("/public/taxi/{taxiId}/bookings/{bookingId}/accept", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Taxi 1 is not found")));
    }

    @Test
    @SneakyThrows
    void shouldHandleIllegalTaxiStatusException() {
        doThrow(new IllegalTaxiStatusException("Illegal taxi status")).when(taxiBookingManagementService).acceptBooking(1L, 1L);

        mockMvc.perform(post("/public/taxi/{taxiId}/bookings/{bookingId}/accept", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Illegal taxi status")));
    }

    @Test
    @SneakyThrows
    void shouldHandleBookingNotFoundException() {
        doThrow(new BookingNotFoundException(1L)).when(bookingService).cancelBooking(1L);

        mockMvc.perform(post("/public/bookings/{bookingId}/cancel", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Booking 1 is not found")));
    }

    @Test
    @SneakyThrows
    void shouldHandleIllegalBookingStatusException() {
        doThrow(new IllegalBookingStatusException("Illegal booking status")).when(bookingService).cancelBooking(1L);

        mockMvc.perform(post("/public/bookings/{bookingId}/cancel", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Illegal booking status")));
    }

}
