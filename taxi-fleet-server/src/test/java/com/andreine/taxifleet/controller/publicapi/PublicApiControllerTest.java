package com.andreine.taxifleet.controller.publicapi;

import com.andreine.taxifleet.controller.BaseControllerTest;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicApiControllerTest extends BaseControllerTest {

    @Test
    @SneakyThrows
    void shouldHandleTaxiNotFoundException() {
        doThrow(new TaxiNotFoundException(1L)).when(taxiBookingManagementService).acceptBooking(1L, 1L);

        mockMvc.perform(post("/public/taxi/{taxiId}/bookings/{bookingId}/accept", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

}
