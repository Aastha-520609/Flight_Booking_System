package com.bookingservice.booking_service.Service;

import com.bookingservice.booking_service.DTO.BookingRequest;
import com.bookingservice.booking_service.DTO.FlightDTO;
import com.bookingservice.booking_service.Entity.Booking;
import com.bookingservice.booking_service.Entity.BookingStatus;
import com.bookingservice.booking_service.Feign.FlightServiceClient;
import com.bookingservice.booking_service.Repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightServiceClient flightServiceClient;

    public BookingService(BookingRepository bookingRepository, FlightServiceClient flightServiceClient) {
        this.bookingRepository = bookingRepository;
        this.flightServiceClient = flightServiceClient;
    }

    @Transactional
    public Booking bookFlight(BookingRequest bookingRequest) {
        // Step 1: Get flight details from flight-service
        FlightDTO flight = flightServiceClient.getFlightById(bookingRequest.getFlightId());

        if (flight == null) {
            throw new RuntimeException("Flight not found!");
        }

        // Step 2: Check seat availability
        if (flight.getSeatsAvailable() < bookingRequest.getSeatsBooked()) {
            throw new RuntimeException("Not enough seats available!");
        }

        // Step 3: Create and save the new booking
        Booking newBooking = new Booking();
        newBooking.setPassengerName(bookingRequest.getPassengerName());
        newBooking.setAge(bookingRequest.getAge());
        newBooking.setEmail(bookingRequest.getEmail());
        newBooking.setContactNumber(bookingRequest.getContactNumber());
        newBooking.setSeatsBooked(bookingRequest.getSeatsBooked());
        newBooking.setFlightId(bookingRequest.getFlightId());
        newBooking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(newBooking);

        // Step 4: Update only seatsAvailable in FlightDTO
        FlightDTO updatedFlight = new FlightDTO();
        updatedFlight.setId(flight.getId());  // Ensure ID is set
        updatedFlight.setAirlineName(flight.getAirlineName());
        updatedFlight.setFlightNumber(flight.getFlightNumber());
        updatedFlight.setSource(flight.getSource());
        updatedFlight.setDestination(flight.getDestination());
        updatedFlight.setFlightDate(flight.getFlightDate());
        updatedFlight.setDepartureTime(flight.getDepartureTime());
        updatedFlight.setArrivalTime(flight.getArrivalTime());
        updatedFlight.setPrice(flight.getPrice());
        updatedFlight.setSeatsAvailable(flight.getSeatsAvailable() - bookingRequest.getSeatsBooked()); 

        flightServiceClient.updateFlight(flight.getId(), updatedFlight);

        return savedBooking;
    }

}
