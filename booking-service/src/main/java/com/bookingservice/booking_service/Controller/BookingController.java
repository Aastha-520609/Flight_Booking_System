package com.bookingservice.booking_service.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Booking> createBooking(
        @RequestBody BookingRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();
        System.out.println("Booking request received from: " + username);
        System.out.println("Booking Details: " + request);

        // Call service to create the booking
        Booking booking = bookingService.createBooking(username, request);

        return ResponseEntity.ok(booking);  // Return saved booking
    }
}
