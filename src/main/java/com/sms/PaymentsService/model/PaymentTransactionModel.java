package com.sms.PaymentsService.model;

import com.sms.PaymentsService.enums.PaymentMethod;
import com.sms.PaymentsService.enums.PaymentStatus;
import com.sms.PaymentsService.enums.Status;
import com.sms.PaymentsService.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionModel{

    private String id;

    private String bankId;

    @NotNull(message = "Service ID is required.")
    private String serviceId;

    private String userId;

    @NotNull(message = "Payment amount is required.")
    private String amount;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private String status;

    private TransactionType transactionType;

    private String createdDate;

    private String updatedDate;

    private String createdBy;

    private String updatedBy;

}
