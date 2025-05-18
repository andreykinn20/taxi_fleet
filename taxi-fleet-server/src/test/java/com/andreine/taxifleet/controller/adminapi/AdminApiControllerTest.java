package com.andreine.taxifleet.controller.adminapi;

import com.andreine.taxifleet.controller.BaseControllerTest;
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

class AdminApiControllerTest extends BaseControllerTest {

    @Test
    @SneakyThrows
    void shouldHandleRuntimeException() {
        when(taxiService.getTaxis()).thenThrow(new RuntimeException("Error occurred"));

        mockMvc.perform(get("/admin/taxis"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message", containsString("Error occurred")));
    }

}
