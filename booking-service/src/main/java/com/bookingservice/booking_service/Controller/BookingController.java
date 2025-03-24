package com.bookingservice.booking_service.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookingservice.booking_service.DTO.BookingRequest;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(
        @RequestParam String username, 
        @RequestBody BookingRequest request
    ) {
        System.out.println("Booking request received from: " + username);
        System.out.println("Booking Details: " + request);
        
        return ResponseEntity.ok("Booking Created Successfully for " + username);
    }
}
