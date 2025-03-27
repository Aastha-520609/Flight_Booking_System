package com.flightservice.flight_service.Service;

import com.flightservice.flight_service.Entity.Flight;
import com.flightservice.flight_service.Exception.FlightNotFoundException;
import com.flightservice.flight_service.Repository.FlightRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.math.BigDecimal;

@Service
public class FlightService {
    
	@Autowired
    private FlightRepository flightRepository;

    public FlightService(FlightRepository searchRepository) {
        this.flightRepository = searchRepository;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate flightDate) {
        return flightRepository.findBySourceAndDestinationAndFlightDate(source, destination, flightDate);
    }

    public Flight searchFlightByNumber(String flightNumber, LocalDate flightDate) {
        return flightRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
    }
    
    public Flight findFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with ID " + id + " not found"));
    }
    
    //add flight by admin
    public List<Flight> addFlights(List<Flight> flights) {
        return flightRepository.saveAll(flights);
    }
    
    //update an existing flight by admin
    public Flight updateFlight(Long id, Flight updatedFlight) {
        Optional<Flight> existingFlightOpt = flightRepository.findById(id);

        if (existingFlightOpt.isPresent()) {
            Flight existingFlight = existingFlightOpt.get();
            existingFlight.setAirlineName(updatedFlight.getAirlineName());
            existingFlight.setFlightNumber(updatedFlight.getFlightNumber());
            existingFlight.setSource(updatedFlight.getSource());
            existingFlight.setDestination(updatedFlight.getDestination());
            existingFlight.setFlightDate(updatedFlight.getFlightDate());
            existingFlight.setDepartureTime(updatedFlight.getDepartureTime());
            existingFlight.setArrivalTime(updatedFlight.getArrivalTime());
            existingFlight.setPrice(updatedFlight.getPrice());
            existingFlight.setSeatsAvailable(updatedFlight.getSeatsAvailable());

            return flightRepository.save(existingFlight);
        }
        return null;
    }
    
    //partialUpdate
    public Flight updateFlightFields(Long id, Map<String, Object> updates) {
        Optional<Flight> existingFlightOpt = flightRepository.findById(id);

        if (existingFlightOpt.isPresent()) {
            Flight existingFlight = existingFlightOpt.get();

            // Manually update fields if they exist in the request
            if (updates.containsKey("airlineName")) {
                existingFlight.setAirlineName((String) updates.get("airlineName"));
            }
            if (updates.containsKey("flightNumber")) {
                existingFlight.setFlightNumber((String) updates.get("flightNumber"));
            }
            if (updates.containsKey("source")) {
                existingFlight.setSource((String) updates.get("source"));
            }
            if (updates.containsKey("destination")) {
                existingFlight.setDestination((String) updates.get("destination"));
            }
            if (updates.containsKey("flightDate")) {
                existingFlight.setFlightDate(LocalDate.parse(updates.get("flightDate").toString()));
            }
            if (updates.containsKey("departureTime")) {
                existingFlight.setDepartureTime(LocalTime.parse(updates.get("departureTime").toString()));
            }
            if (updates.containsKey("arrivalTime")) {
                existingFlight.setArrivalTime(LocalTime.parse(updates.get("arrivalTime").toString()));
            }
            if (updates.containsKey("price")) {
                existingFlight.setPrice(new BigDecimal(updates.get("price").toString()));
            }
            if (updates.containsKey("seatsAvailable")) {
                existingFlight.setSeatsAvailable(Integer.parseInt(updates.get("seatsAvailable").toString()));
            }

            return flightRepository.save(existingFlight);
        }
        return null;
    }
    
    //delete flight by admin
    public boolean deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
