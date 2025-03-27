package com.bookingservice.booking_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	private String passengerName;
    private int age;
    private String email;
    private String contactNumber;
    private int seatsBooked; 
    private Long flightId; 
}
