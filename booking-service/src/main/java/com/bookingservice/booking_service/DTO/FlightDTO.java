package com.bookingservice.booking_service.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private Long id;
    private String airlineName;
    private String flightNumber;
    private String source;
    private String destination;
    private LocalDate flightDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal price;
    private int seatsAvailable;
}
