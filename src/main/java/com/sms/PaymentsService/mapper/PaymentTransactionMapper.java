package com.sms.PaymentsService.mapper;

import com.sms.PaymentsService.entity.PaymentTransaction;
import com.sms.PaymentsService.exception.custom.InvalidDateFormatException;
import com.sms.PaymentsService.model.PaymentTransactionModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionMapper {

    public PaymentTransaction toEntity(PaymentTransactionModel paymentTransactionModel) throws InvalidDateFormatException {
            PaymentTransaction paymentTransaction = new PaymentTransaction();

            paymentTransaction.setBankId(Integer.parseInt(paymentTransactionModel.getBankId()));
            paymentTransaction.setSubscriptionId(Integer.parseInt(paymentTransactionModel.getSubscriptionId()));
            paymentTransaction.setAmount(Double.parseDouble(paymentTransactionModel.getAmount()));
            paymentTransaction.setPaymentStatus(paymentTransactionModel.getPaymentStatus());
            paymentTransaction.setPaymentMethod(paymentTransactionModel.getPaymentMethod());
            paymentTransaction.setStatus(paymentTransactionModel.getStatus());
            paymentTransaction.setTransactionType(paymentTransactionModel.getTransactionType());

            return paymentTransaction;
        }

    public PaymentTransactionModel toModel(PaymentTransaction paymentTransaction) throws InvalidDateFormatException {
        PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
        paymentTransactionModel.setId(String.valueOf(paymentTransaction.getId()));
        paymentTransactionModel.setBankId(String.valueOf(paymentTransaction.getBankId()));
        paymentTransactionModel.setSubscriptionId(String.valueOf(paymentTransaction.getSubscriptionId()));
        paymentTransactionModel.setAmount(String.valueOf(paymentTransaction.getAmount()));
        paymentTransactionModel.setPaymentStatus(paymentTransaction.getPaymentStatus());
        paymentTransactionModel.setPaymentMethod(paymentTransaction.getPaymentMethod());
        paymentTransactionModel.setStatus(paymentTransaction.getStatus());
        paymentTransactionModel.setTransactionType(paymentTransaction.getTransactionType());

        return paymentTransactionModel;
    }

}
