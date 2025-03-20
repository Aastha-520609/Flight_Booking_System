package com.flightsearch.search_service.Service;

import com.flightsearch.search_service.Entity.Flight;
import com.flightsearch.search_service.Repository.SearchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    
    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate flightDate) {
        return searchRepository.findBySourceAndDestinationAndFlightDate(source, destination, flightDate);
    }

    public Flight searchFlightByNumber(String flightNumber, LocalDate flightDate) {
        return searchRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
    }
}
