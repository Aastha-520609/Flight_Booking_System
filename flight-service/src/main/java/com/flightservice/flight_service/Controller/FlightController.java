package com.flightservice.flight_service.Controller;

import com.flightservice.flight_service.Entity.Flight;
import com.flightservice.flight_service.Exception.FlightNotFoundException;
import com.flightservice.flight_service.Exception.InvalidFlightDateException;
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

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> findFlightsBySourceAndDestination(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {

        if (flightDate.isBefore(LocalDate.now())) {
            throw new InvalidFlightDateException("Past date flights are not available.");
        }

        List<Flight> flights = flightService.searchFlights(source, destination, flightDate);

        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found for the given source, destination, and date.");
        }

        return ResponseEntity.ok(flights);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Long id) {
        Flight flight = flightService.findFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Flight> findFlightByFlightNumberAndDate(
            @PathVariable String flightNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {

        Flight flight = flightService.searchFlightByNumber(flightNumber, flightDate);

        if (flight == null) {
            throw new FlightNotFoundException("No flight found for flight number: " + flightNumber + " on date: " + flightDate);
        }

        return ResponseEntity.ok(flight);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFlights(@RequestBody List<Flight> flights) {
        boolean hasPastDates = flights.stream()
                .anyMatch(flight -> flight.getFlightDate().isBefore(LocalDate.now()));

        if (hasPastDates) {
            throw new InvalidFlightDateException("Cannot add flights for past dates.");
        }

        flightService.addFlights(flights);
        return ResponseEntity.ok("Flights added successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        if (updatedFlight.getFlightDate().isBefore(LocalDate.now())) {
            throw new InvalidFlightDateException("Cannot update flight to a past date.");
        }

        Flight flight = flightService.updateFlight(id, updatedFlight);
        return ResponseEntity.ok(flight);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Flight> updateFlightFields(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Flight updatedFlight = flightService.updateFlightFields(id, updates);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        boolean isDeleted = flightService.deleteFlight(id);
        if (!isDeleted) {
            throw new FlightNotFoundException("Flight with ID " + id + " not found.");
        }
        return ResponseEntity.ok("Flight deleted successfully.");
    }
}
