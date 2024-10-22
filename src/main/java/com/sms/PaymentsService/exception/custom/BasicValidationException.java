package com.sms.PaymentsService.exception.custom;

public class BasicValidationException extends RuntimeException {
    public BasicValidationException(String message) {
        super(message);
    }
}
