package com.flightservice.flight_service.Service;

import com.flightservice.flight_service.Entity.Flight;
import com.flightservice.flight_service.Repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    
    @Mock
    private FlightRepository flightRepository;
    
    //Injects the mocked repository into FlightService, so it doesn’t use a real database.
    	
    @InjectMocks
    private FlightService flightService;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId(1);
        flight.setAirlineName("Air India");
        flight.setFlightNumber("AI123");
        flight.setSource("DEL");
        flight.setDestination("MUM");
        flight.setFlightDate(LocalDate.of(2025, 3, 25));
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setArrivalTime(LocalTime.of(12, 30));
        flight.setPrice(new BigDecimal("3500"));
        flight.setSeatsAvailable(100);
    }

    @Test
    void testSearchFlights() {
        when(flightRepository.findBySourceAndDestinationAndFlightDate("DEL", "MUM", flight.getFlightDate()))
                .thenReturn(Collections.singletonList(flight));

        List<Flight> flights = flightService.searchFlights("DEL", "MUM", flight.getFlightDate());
        assertFalse(flights.isEmpty());
        assertEquals(1, flights.size());
        assertEquals("AI123", flights.get(0).getFlightNumber());
    }

    @Test
    void testSearchFlightByNumber() {
        when(flightRepository.findByFlightNumberAndFlightDate("AI123", flight.getFlightDate()))
                .thenReturn(flight);

        Flight result = flightService.searchFlightByNumber("AI123", flight.getFlightDate());
        assertNotNull(result);
        assertEquals("Air India", result.getAirlineName());
    }

    @Test
    void testAddFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        Flight savedFlight = flightService.addFlight(flight);
        assertNotNull(savedFlight);
        assertEquals("AI123", savedFlight.getFlightNumber());
    }

    @Test
    void testUpdateFlight() {
        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        Flight updatedFlight = new Flight();
        updatedFlight.setAirlineName("Indigo");
        updatedFlight.setFlightNumber("6E456");
        updatedFlight.setSource("DEL");
        updatedFlight.setDestination("BLR");
        updatedFlight.setFlightDate(LocalDate.of(2025, 3, 26));
        updatedFlight.setDepartureTime(LocalTime.of(14, 30));
        updatedFlight.setArrivalTime(LocalTime.of(16, 30));
        updatedFlight.setPrice(new BigDecimal("4500"));
        updatedFlight.setSeatsAvailable(120);

        Flight result = flightService.updateFlight(1, updatedFlight);
        assertNotNull(result);
        assertEquals("Indigo", result.getAirlineName());
    }

    @Test
    void testUpdateFlightFields() {
        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("airlineName", "SpiceJet");
        updates.put("price", "5500");

        Flight result = flightService.updateFlightFields(1, updates);
        assertNotNull(result);
        assertEquals("SpiceJet", result.getAirlineName());
        assertEquals(new BigDecimal("5500"), result.getPrice());
    }

    @Test
    void testDeleteFlight() {
        when(flightRepository.existsById(1)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(1);
        
        boolean isDeleted = flightService.deleteFlight(1);
        assertTrue(isDeleted);
        verify(flightRepository, times(1)).deleteById(1);
    }
}
