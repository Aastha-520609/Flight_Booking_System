package com.bookingservice.booking_service.Feign;

import com.bookingservice.booking_service.DTO.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "flight-service", url = "http://localhost:8080/flights")
public interface FlightServiceClient {

    @GetMapping("/search")
    List<FlightDTO> searchFlights(@RequestParam String source, 
                                  @RequestParam String destination, 
                                  @RequestParam String flightDate);

    @GetMapping("/{flightId}")
    FlightDTO getFlightById(@PathVariable Long flightId);

    @GetMapping("/getFlightDetails")
    FlightDTO getFlightDetails( @RequestParam String source, 
                               @RequestParam String destination, 
                               @RequestParam String flightDate);

    @GetMapping("/checkAvailability")
    boolean checkAvailability(@RequestParam Long flightId, @RequestParam int numSeats);

    @GetMapping("/{flightId}/price")
    int getFlightPrice(@PathVariable Long flightId);

    @GetMapping("/{flightId}/seatsAvailable")
    int getAvailableSeats(@PathVariable Long flightId);
    
    @PostMapping("/{flightId}/reduceSeats")
    void reduceSeats(@PathVariable Long flightId, @RequestParam int numSeats);
}
