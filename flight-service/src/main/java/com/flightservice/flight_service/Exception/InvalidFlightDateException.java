package com.flightservice.flight_service.Exception;

public class InvalidFlightDateException extends RuntimeException {
    public InvalidFlightDateException(String message) {
        super(message);
    }
}
