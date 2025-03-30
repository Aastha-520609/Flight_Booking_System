package com.paymentservice.payment_service.Service;

import com.paymentservice.payment_service.Client.BookingClient;
import com.paymentservice.payment_service.DTO.BookingResponse;
import com.paymentservice.payment_service.Entity.Payment;
import com.paymentservice.payment_service.Exception.PaymentProcessingException;
import com.paymentservice.payment_service.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    public PaymentService(PaymentRepository paymentRepository, BookingClient bookingClient) {
        this.paymentRepository = paymentRepository;
        this.bookingClient = bookingClient;
    }

    public Payment processPayment(Long bookingId) {
        Stripe.apiKey = stripeSecretKey;

        BigDecimal amount = fetchAmountFromBookingService(bookingId);
        logger.info("Received amount from Booking Service: {}", amount);
        
        if (amount == null) {
            logger.error("Booking ID {} has null amount!", bookingId);
            throw new IllegalArgumentException("Booking amount cannot be null");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentProcessingException("Invalid payment amount: " + amount);
        }

        // Create Stripe PaymentIntent
        String transactionId = createStripePaymentIntent(amount);

        // Save payment details in DB
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .transactionId(transactionId)
                .paymentStatus("PENDING") // Initially pending
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    private BigDecimal fetchAmountFromBookingService(Long bookingId) {
        try {
        	logger.info("Calling Booking Service for bookingId: {}", bookingId);
            BookingResponse response = bookingClient.getBooking(bookingId);
            logger.info("Received response from Booking Service: {}", response);
            if (response == null) {
                logger.error("Booking API returned null for bookingId {}", bookingId);
                throw new PaymentProcessingException("Invalid response from Booking Service");
            }
            logger.info("Fetched booking amount: {}", response.getTotalAmount());
            return response.getTotalAmount();
        } catch (Exception e) {
            logger.error("Error fetching booking amount for bookingId {}: {}", bookingId, e.getMessage());
            throw new PaymentProcessingException("Failed to retrieve booking amount from Booking Service");
        }
    }

    private String createStripePaymentIntent(BigDecimal amount) {
        try {
        	logger.info("Creating Stripe Payment Intent for amount: {}", amount);
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // Convert INR to paisa
                            .setCurrency("inr")
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);
            return intent.getId();
        } catch (Exception e) {
            logger.error("Stripe PaymentIntent creation failed: {}", e.getMessage());
            throw new PaymentProcessingException("Failed to create Stripe payment");
        }
    }
}
