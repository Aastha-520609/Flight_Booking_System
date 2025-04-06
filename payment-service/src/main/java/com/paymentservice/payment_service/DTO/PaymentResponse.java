package com.paymentservice.payment_service.DTO;

public class PaymentResponse {
    private Long paymentId;
    private String stripeCheckoutUrl;

    public PaymentResponse(Long paymentId, String stripeCheckoutUrl) {
        this.paymentId = paymentId;
        this.stripeCheckoutUrl = stripeCheckoutUrl;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getStripeCheckoutUrl() {
        return stripeCheckoutUrl;
    }
}
