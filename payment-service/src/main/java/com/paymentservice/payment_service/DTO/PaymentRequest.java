package com.paymentservice.payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long bookingId;
    
    @Override
    public String toString() {
        return "PaymentRequest{ bookingId=" + bookingId + " }";
    }
}
