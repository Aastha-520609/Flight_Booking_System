package com.bookingservice.booking_service.Controller;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.DTO.BookingResponse;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Repository.BookingRepository;
import com.bookingservice.booking_service.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
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
    
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getTotalAmount() == null) {
            throw new RuntimeException("Total amount is null for booking ID: " + bookingId);
        }

        BookingResponse response = new BookingResponse(booking.getId(), booking.getTotalAmount());
        return ResponseEntity.ok(response);
    }
}
