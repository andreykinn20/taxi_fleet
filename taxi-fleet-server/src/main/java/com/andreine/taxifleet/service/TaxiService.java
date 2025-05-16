package com.andreine.taxifleet.service;

import java.util.List;

import com.andreine.taxifleet.converter.TaxiConverter;
import com.andreine.taxifleet.exception.IllegalTaxiStatusException;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import com.andreine.taxifleet.persistence.model.TaxiStatus;
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
            .map(TaxiConverter::fromEntity)
            .toList();
    }

    /**
     * Sets taxi unavailable.
     *
     * @param taxiId taxi id
     */
    public void setTaxiUnavailable(long taxiId) {
        var taxi = taxiRepository.findById(taxiId)
            .orElseThrow(() -> new TaxiNotFoundException(taxiId));

        if (!TaxiStatus.AVAILABLE.equals(taxi.getStatus())) {
            throw new IllegalTaxiStatusException("Taxi %s is not available".formatted(taxiId));
        }

        taxiRepository.save(taxi.toBuilder()
            .status(TaxiStatus.UNAVAILABLE)
            .build());
    }

    /**
     * Sets taxi available.
     *
     * @param taxiId taxi id
     */
    public void setTaxiAvailable(long taxiId) {
        var taxi = taxiRepository.findById(taxiId)
            .orElseThrow(() -> new TaxiNotFoundException(taxiId));

        if (!TaxiStatus.UNAVAILABLE.equals(taxi.getStatus())) {
            throw new IllegalTaxiStatusException("Taxi %s is not unavailable".formatted(taxiId));
        }

        taxiRepository.save(taxi.toBuilder()
            .status(TaxiStatus.AVAILABLE)
            .build());
    }

}
