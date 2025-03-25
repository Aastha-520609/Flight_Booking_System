package com.bookingservice.booking_service.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private String name;
    private int age;
    private String email;
    private String phone;
    private int numSeats;
    private Long flightId; 
}
