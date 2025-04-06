package com.paymentservice.payment_service.Service;

import com.paymentservice.payment_service.Client.BookingClient;
import com.paymentservice.payment_service.DTO.BookingResponse;
import com.paymentservice.payment_service.DTO.PaymentResponse;
import com.paymentservice.payment_service.Entity.Payment;
import com.paymentservice.payment_service.Exception.PaymentProcessingException;
import com.paymentservice.payment_service.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
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

    public PaymentResponse processPayment(Long bookingId) {
        Stripe.apiKey = stripeSecretKey;

        BigDecimal amount = fetchAmountFromBookingService(bookingId);
        logger.info("Received amount from Booking Service: {}", amount);

        if (amount == null) {
            throw new IllegalArgumentException("Booking amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentProcessingException("Invalid payment amount: " + amount);
        }

        // Create Stripe Checkout Session and get session ID + URL
        Session session = createStripeCheckoutSession(amount);

        // Save payment details in DB
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .transactionId(session.getId())
                .paymentStatus("PENDING")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Return both saved payment and checkout URL
        return new PaymentResponse(savedPayment.getId(), session.getUrl());
    }

    private BigDecimal fetchAmountFromBookingService(Long bookingId) {
        try {
            logger.info("Calling Booking Service for bookingId: {}", bookingId);
            BookingResponse response = bookingClient.getBooking(bookingId);
            logger.info("Received response from Booking Service: {}", response);

            if (response == null || response.getTotalAmount() == null) {
                throw new PaymentProcessingException("Invalid response from Booking Service");
            }

            return response.getTotalAmount();
        } catch (Exception e) {
            logger.error("Error fetching booking amount: {}", e.getMessage());
            throw new PaymentProcessingException("Failed to retrieve booking amount from Booking Service");
        }
    }

    private Session createStripeCheckoutSession(BigDecimal amount) {
        try {
            logger.info("Creating Stripe Checkout Session for amount: {}", amount);

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("https://example.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // INR → paisa
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Booking Payment")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            logger.info("Stripe Checkout Session created: {}", session.getId());
            return session;

        } catch (Exception e) {
            logger.error("Stripe Checkout Session creation failed: {}", e.getMessage());
            throw new PaymentProcessingException("Failed to create Stripe Checkout Session");
        }
    }
}
