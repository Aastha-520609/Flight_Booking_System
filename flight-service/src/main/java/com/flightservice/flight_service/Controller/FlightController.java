package com.flightservice.flight_service.Controller;

import com.flightservice.flight_service.Entity.Flight;
import com.flightservice.flight_service.Feign.UserServiceClient;
import com.flightservice.flight_service.Service.FlightService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final UserServiceClient userServiceClient;

    public FlightController(FlightService flightService, UserServiceClient userServiceClient) {
        this.flightService = flightService;
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> findFlightsBySourceAndDestination(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {
        
        List<Flight> flights = flightService.searchFlights(source, destination, flightDate);
        
        return flights.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(flights);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Flight> findFlightByFlightNumberAndDate(
            @PathVariable String flightNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {
        
        Flight flight = flightService.searchFlightByNumber(flightNumber, flightDate);
        
        return (flight != null) ? ResponseEntity.ok(flight) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id);
        return (flight != null) ? ResponseEntity.ok(flight) : ResponseEntity.notFound().build();
    }
    
    
    //add
    @PostMapping("/add")
    public Flight addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }
    
    //fullUpdate
    @PutMapping("/update/{id}")
    public Flight updateFlight(@PathVariable Integer id, @RequestBody Flight updatedFlight) {
        return flightService.updateFlight(id, updatedFlight);
    }
    
    //partialUpdate
    @PatchMapping("/update/{id}")
    public ResponseEntity<Flight> updateFlightFields(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        
        Flight updatedFlight = flightService.updateFlightFields(id, updates);
        
        return (updatedFlight != null) ? ResponseEntity.ok(updatedFlight) : ResponseEntity.notFound().build();
    }
    
    //delete
    @DeleteMapping("/delete/{id}")
    public String deleteFlight(@PathVariable Integer id) {
        boolean isDeleted = flightService.deleteFlight(id);
        return isDeleted ? "Flight deleted successfully." : "Flight not found.";
    }
    
    @GetMapping("/check-user")
    public ResponseEntity<String> checkUser(@RequestHeader("Authorization") String token) {
        return userServiceClient.validateToken(token);
    }
}