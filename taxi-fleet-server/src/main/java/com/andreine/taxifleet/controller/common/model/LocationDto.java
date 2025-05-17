package com.andreine.taxifleet.controller.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location DTO.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LocationDto {

    private Double latitude;

    private Double longitude;

}