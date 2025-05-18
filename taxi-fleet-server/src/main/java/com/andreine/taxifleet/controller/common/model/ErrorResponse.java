package com.andreine.taxifleet.controller.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error response.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorResponse {

    String message;

    Integer errorCode;

}
