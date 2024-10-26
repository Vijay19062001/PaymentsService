package com.sms.PaymentsService.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private String transactionId;
    private String status;
    private String message;
}
