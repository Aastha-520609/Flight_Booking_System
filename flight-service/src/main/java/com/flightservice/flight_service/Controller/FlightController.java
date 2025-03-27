package com.flightservice.flight_service.Controller;

import com.flightservice.flight_service.Entity.Flight;
//import com.flightservice.flight_service.Feign.UserServiceClient;
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
    //private final UserServiceClient userServiceClient;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
        //this.userServiceClient = userServiceClient;
    }

    @GetMapping("/search")
    public ResponseEntity<?> findFlightsBySourceAndDestination(
            @RequestParam(required = true) String source,
            @RequestParam(required = true) String destination,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {
    	
    	if (flightDate.isBefore(LocalDate.now())) {
            return ResponseEntity.status(404).body("Past date flights are not available.");
        }
        
        List<Flight> flights = flightService.searchFlights(source, destination, flightDate);
        
        return flights.isEmpty() 
                ? ResponseEntity.status(404).body("No flights found for the given source, destination, and date.") 
                : ResponseEntity.ok(flights);
    }
    
    //added inorder to get flight in booking
    @GetMapping("/id/{id}")
    public ResponseEntity<?> findFlightById(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id);
        return (flight != null) 
            ? ResponseEntity.ok(flight) 
            : ResponseEntity.status(404).body("Flight with ID " + id + " not found.");
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<?> findFlightByFlightNumberAndDate(
            @PathVariable String flightNumber,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {
        
        Flight flight = flightService.searchFlightByNumber(flightNumber, flightDate);
        
        return (flight != null) 
            ? ResponseEntity.ok(flight) 
            : ResponseEntity.status(404).body("No flight found for flight number: " + flightNumber + " on date: " + flightDate);
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addFlights(@RequestBody List<Flight> flights) {
        for (Flight flight : flights) {
            if (flight.getFlightDate().isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body("Cannot add flights for past dates.");
            }
        }

        flightService.addFlights(flights);
        return ResponseEntity.ok("Flights added successfully.");
    }

    
    //fullUpdate
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        Flight existingFlight = flightService.findFlightById(id);
        if (existingFlight == null) {
            return ResponseEntity.status(404).body("Flight with ID " + id + " not found.");
        }

        if (updatedFlight.getFlightDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Cannot update flight to a past date.");
        }

        Flight flight = flightService.updateFlight(id, updatedFlight);
        return ResponseEntity.ok(flight);
    }
    
    //partialUpdate
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateFlightFields(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        
        Flight updatedFlight = flightService.updateFlightFields(id, updates);
        
        return (updatedFlight != null) 
                ? ResponseEntity.ok(updatedFlight) 
                : ResponseEntity.status(404).body("Flight with ID " + id + " not found for update.");
    }
    
    //delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        boolean isDeleted = flightService.deleteFlight(id);
        
        if (isDeleted) {
            return ResponseEntity.ok("Flight deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Flight not found.");
        }
    }
   
}