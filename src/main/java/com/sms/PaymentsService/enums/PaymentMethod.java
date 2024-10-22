package com.sms.PaymentsService.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    DEBIT("debit"),
    CREDIT("credit");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static PaymentMethod fromString(String paymentMethod) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getPaymentMethod().equalsIgnoreCase(paymentMethod)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No enum constant found for payment method: " + paymentMethod);
    }

}
