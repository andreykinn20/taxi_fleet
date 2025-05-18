package com.andreine.taxifleet.controller.common.model;

import com.andreine.taxifleet.controller.adminapi.AdminApiController;
import com.andreine.taxifleet.controller.publicapi.PublicApiController;
import com.andreine.taxifleet.exception.BookingNotFoundException;
import com.andreine.taxifleet.exception.IllegalBookingStatusException;
import com.andreine.taxifleet.exception.IllegalTaxiStatusException;
import com.andreine.taxifleet.exception.TaxiNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * API exception handler.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {PublicApiController.class, AdminApiController.class})
@RequiredArgsConstructor
public class ApiExceptionHandler {

    /**
     * Common handler for {@link Exception}.
     * The status code 500 is applied to the HTTP response when the handler method is invoked.
     *
     * @param exception an exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        var errorResponse = ErrorResponse.builder()
            .message(exception.getMessage())
            .build();

        log.error("Internal server error {}", errorResponse, exception);

        return errorResponse;
    }

    /**
     * Common handler for {@link BookingNotFoundException}.
     * The status code 400 is applied to the HTTP response when the handler method is invoked.
     *
     * @param exception booking not found exception
     * @return error response
     */
    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(BookingNotFoundException exception) {
        return processBadRequest(exception);
    }

    /**
     * Common handler for {@link TaxiNotFoundException}.
     * The status code 400 is applied to the HTTP response when the handler method is invoked.
     *
     * @param exception taxi not found exception
     * @return error response
     */
    @ExceptionHandler(TaxiNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(TaxiNotFoundException exception) {
        return processBadRequest(exception);
    }

    /**
     * Common handler for {@link IllegalTaxiStatusException}.
     * The status code 400 is applied to the HTTP response when the handler method is invoked.
     *
     * @param exception illegal taxi status exception
     * @return error response
     */
    @ExceptionHandler(IllegalTaxiStatusException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(IllegalTaxiStatusException exception) {
        return processBadRequest(exception);
    }

    /**
     * Common handler for {@link IllegalBookingStatusException}.
     * The status code 400 is applied to the HTTP response when the handler method is invoked.
     *
     * @param exception illegal booking status exception
     * @return error response
     */
    @ExceptionHandler(IllegalBookingStatusException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(IllegalBookingStatusException exception) {
        return processBadRequest(exception);
    }

    private ErrorResponse processBadRequest(Exception exception) {
        var errorResponse = ErrorResponse.builder()
            .message(exception.getMessage())
            .build();

        log.warn("Bad request {}", errorResponse, exception);

        return errorResponse;
    }

}
