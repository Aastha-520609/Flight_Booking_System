//package com.bookingservice.booking_service.Feign;
//
//import com.bookingservice.booking_service.DTO.PaymentRequest;
//import com.bookingservice.booking_service.DTO.BookingResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//@FeignClient(name = "payment-service", url = "http://localhost:8085")
//public interface PaymentServiceClient {
//
//	 @PostMapping("/payments/process")
//	 String processPayment(@RequestBody PaymentRequest paymentRequest);
//}
//
