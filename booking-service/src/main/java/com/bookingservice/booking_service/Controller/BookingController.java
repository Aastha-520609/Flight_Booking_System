package com.bookingservice.booking_service.Controller;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@RequestBody BookingRequest bookingRequest) {
        System.out.println("Received booking: " + bookingRequest);
        try {
            Booking bookedFlight = bookingService.bookFlight(bookingRequest);
            return ResponseEntity.ok(bookedFlight);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
