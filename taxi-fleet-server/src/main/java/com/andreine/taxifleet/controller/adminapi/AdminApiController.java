package com.andreine.taxifleet.controller.adminapi;

import java.util.List;

import com.andreine.taxifleet.controller.adminapi.model.TaxiDto;
import com.andreine.taxifleet.converter.TaxiConverter;
import com.andreine.taxifleet.service.TaxiService;
import lombok.RequiredArgsConstructor;
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

    /**
     * Gets taxis.
     *
     * @return taxis
     */
    public List<TaxiDto> getTaxis() {
        return taxiService.getTaxis().stream()
            .map(TaxiConverter::convert)
            .toList();
    }

}
