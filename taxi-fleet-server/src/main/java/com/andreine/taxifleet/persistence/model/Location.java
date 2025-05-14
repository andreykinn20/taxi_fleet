package com.andreine.taxifleet.persistence.model;

import jakarta.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Location {

    private Double originLatitude;

    private Double originLongitude;

    private Double destinationLatitude;

    private Double destinationLongitude;

}
