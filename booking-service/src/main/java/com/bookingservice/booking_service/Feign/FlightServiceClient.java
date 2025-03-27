package com.bookingservice.booking_service.Feign;

import com.bookingservice.booking_service.DTO.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "flight-service", url = "http://localhost:8081")
public interface FlightServiceClient {

	@GetMapping("/flights/id/{id}")
    FlightDTO getFlightById(@PathVariable("id") Long id);


    @PutMapping("/flights/update/{id}") 
	FlightDTO updateFlight(@PathVariable("id") Integer id, @RequestBody FlightDTO updatedFlight);
}
