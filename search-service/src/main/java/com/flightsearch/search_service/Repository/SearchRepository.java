package com.flightsearch.search_service.Repository;
import com.flightsearch.search_service.Entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Flight, Integer> {
    
	List<Flight> findBySourceAndDestinationAndFlightDate(String source, String destination, LocalDate flightDate);
    
	Flight findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);
}
