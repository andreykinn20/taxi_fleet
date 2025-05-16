package com.andreine.taxifleet.service;

import java.util.List;

import com.andreine.taxifleet.converter.TaxiConverter;
import com.andreine.taxifleet.persistence.repository.TaxiRepository;
import com.andreine.taxifleet.service.model.Taxi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Taxi service.
 */
@Service
@RequiredArgsConstructor
public class TaxiService {

    private final TaxiRepository taxiRepository;

    /**
     * Gets taxis.
     *
     * @return taxis
     */
    public List<Taxi> getTaxis() {
        return taxiRepository.findAll().stream()
            .map(TaxiConverter::convert)
            .toList();
    }

}
