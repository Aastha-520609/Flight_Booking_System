package com.bookingservice.booking_service.Entity;

public enum BookingStatus {
    PENDING,   // When booking is created but not yet confirmed
    CONFIRMED, // When booking is successfully completed
    CANCELLED  // When booking is cancelled
}
