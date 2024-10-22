package com.sms.PaymentsService.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    ACTIVATION("activation"),
    DEACTIVATION("deactivation"),
    RENEWAL("renewal"),
    CANCELLATION("cancellation");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public static TransactionType fromString(String type) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getType().equalsIgnoreCase(type)) {
                return transactionType;
            }
        }
        throw new IllegalArgumentException("No enum constant found for type: " + type);
    }
}
