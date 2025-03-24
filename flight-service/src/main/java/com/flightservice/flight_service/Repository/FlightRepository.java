package com.flightservice.flight_service.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightservice.flight_service.Entity.Flight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    
	List<Flight> findBySourceAndDestinationAndFlightDate(String source, String destination, LocalDate flightDate);
    
	Flight findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);
	
	Optional<Flight> findById(Long id);
}
