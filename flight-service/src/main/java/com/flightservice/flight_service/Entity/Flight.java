package com.flightservice.flight_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //Makes Object Creation Easier & Readable
public class Flight {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "airline_name", nullable = false, length = 100)
    private String airlineName;

    @Column(name = "flight_number", nullable = false, unique = true, length = 50)
    private String flightNumber;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "destination", nullable = false, length = 50)
    private String destination;
    
    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "seats_available", nullable = false)
    private int seatsAvailable;
}