package com.paymentservice.payment_service.Client;

import com.paymentservice.payment_service.DTO.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", url = "http://localhost:8082")
public interface BookingClient {

    @GetMapping("/bookings/{bookingId}")
    BookingResponse getBooking(@PathVariable("bookingId") Long bookingId);
}
