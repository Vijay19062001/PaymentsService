package com.sms.PaymentsService.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PaymentStatus {

    SUCCESS("success"),
    FAILED("failed"),
    PENDING("pending");

    private final String paymentStatus;

    PaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static PaymentStatus fromString(String status) {
        for (PaymentStatus ps : PaymentStatus.values()) {
            if (ps.paymentStatus.equalsIgnoreCase(status)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("No enum constant found for paymentStatus: " + status);
    }

}
