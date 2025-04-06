package com.paymentservice.payment_service.Controller;

import com.paymentservice.payment_service.DTO.PaymentRequest;
import com.paymentservice.payment_service.DTO.PaymentResponse;
import com.paymentservice.payment_service.Entity.Payment;
import com.paymentservice.payment_service.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse processedPayment = paymentService.processPayment(paymentRequest.getBookingId());
        return ResponseEntity.ok(processedPayment);
    }

//    @GetMapping("/{transactionId}")
//    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
//        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
//    }
//
//    @GetMapping("/booking/{bookingId}")
//    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable Long bookingId) {
//        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
//    }
}
