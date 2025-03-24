package com.bookingservice.booking_service.Service;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking createBooking(String username, String source, String destination, String date, BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setPassengerName(bookingRequest.getName());
        booking.setPassengerEmail(bookingRequest.getEmail());
        booking.setPassengerPhone(bookingRequest.getPhone());
        booking.setSeatNumber("TBD"); // Seat assignment logic needed
        booking.setTotalPrice(BigDecimal.ZERO); // Pricing logic needed
        booking.setBookingDate(LocalDate.parse(date));
        return bookingRepository.save(booking);
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }
}
