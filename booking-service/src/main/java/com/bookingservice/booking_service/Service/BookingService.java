package com.bookingservice.booking_service.Service;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.DTO.FlightDTO;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Feign.FlightServiceClient;
import com.bookingservice.booking_service.Feign.UserServiceClient;
import com.bookingservice.booking_service.Repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserServiceClient userServiceClient;
    private final FlightServiceClient flightServiceClient;

    public BookingService(BookingRepository bookingRepository, UserServiceClient userServiceClient, FlightServiceClient flightServiceClient) {
        this.bookingRepository = bookingRepository;
        this.userServiceClient = userServiceClient;
        this.flightServiceClient = flightServiceClient;
    }

    @Transactional
    public Booking createBooking(String username, BookingRequest bookingRequest) {
    	Long userId = userServiceClient.getUserIdByUsername(username);
    	Long flightId = bookingRequest.getFlightId();
    	
    	 FlightDTO flight = flightServiceClient.getFlightById(flightId);
    	    if (flight == null) {
    	        throw new RuntimeException("Flight not found.");
    	    }

    	    boolean isAvailable = flightServiceClient.checkAvailability(flightId, bookingRequest.getNumSeats());
    	    if (!isAvailable) {
    	        throw new RuntimeException("No available seats.");
    	    }
    	    
    	
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFlightId(flightId);
        booking.setPassengerName(bookingRequest.getName());
        booking.setPassengerEmail(bookingRequest.getEmail());
        booking.setPassengerPhone(bookingRequest.getPhone());
        booking.setSeatNumber("TBD"); // Seat assignment logic needed
        booking.setTotalPrice(flight.getPrice().multiply(BigDecimal.valueOf(bookingRequest.getNumSeats()))); 
        booking.setBookingDate(LocalDate.now());
        
        flightServiceClient.reduceSeats(flightId, bookingRequest.getNumSeats());
        
        return bookingRepository.save(booking);
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }
}
