package com.andreine.taxifleet.controller;

import com.andreine.taxifleet.TaxiFleetTestApplication;
import com.andreine.taxifleet.service.TaxiBookingManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaxiFleetTestApplication.class)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected TaxiBookingManagementService taxiBookingManagementService;

}
