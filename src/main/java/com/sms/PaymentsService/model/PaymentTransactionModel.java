package com.sms.PaymentsService.model;

import com.sms.PaymentsService.enums.PaymentMethod;
import com.sms.PaymentsService.enums.PaymentStatus;
import com.sms.PaymentsService.enums.Status;
import com.sms.PaymentsService.enums.TransactionType;
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

    private String subscriptionId;

    private String userId;

    private String amount;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private Status status;

    private TransactionType transactionType;

    private String createdDate;

    private String updatedDate;

    private String createdBy;

    private String updatedBy;

}
