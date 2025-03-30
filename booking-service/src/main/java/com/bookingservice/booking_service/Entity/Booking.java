package com.bookingservice.booking_service.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private int age;
    private String contactNumber;
    private String email;
    private int seatsBooked;

    private Long flightId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING; // Default status

    @CreationTimestamp
    private LocalDateTime createdAt; 
    
    private BigDecimal totalAmount;
}
