package com.paymentservice.payment_service.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId; // Reference to booking service

    private BigDecimal amount;

    private String paymentStatus;

    private String transactionId;

    private LocalDateTime paymentDate;
}
