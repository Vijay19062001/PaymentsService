package com.sms.PaymentsService.mapper;

import com.sms.PaymentsService.entity.PaymentTransaction;
import com.sms.PaymentsService.enums.PaymentStatus;
import com.sms.PaymentsService.enums.Status;
import com.sms.PaymentsService.exception.custom.InvalidDateFormatException;
import com.sms.PaymentsService.model.PaymentTransactionModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentTransactionMapper {

    public PaymentTransaction toEntity(PaymentTransactionModel paymentTransactionModel) throws InvalidDateFormatException {
            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setBankId(Integer.parseInt(paymentTransactionModel.getBankId()));
            paymentTransaction.setServiceId(Integer.parseInt(paymentTransactionModel.getServiceId()));
            paymentTransaction.setAmount(Double.parseDouble(paymentTransactionModel.getAmount()));
          paymentTransaction.setPaymentStatus(PaymentStatus.SUCCESS);
          paymentTransaction.setStatus(Status.fromString(paymentTransactionModel.getStatus()));
          paymentTransaction.setAmount(Double.valueOf(paymentTransaction.getAmount()));
          paymentTransaction.setCreatedDate(LocalDateTime.now());
          paymentTransaction.setUpdatedDate(LocalDateTime.now());
          paymentTransaction.setCreatedBy("system");
          paymentTransaction.setUpdatedBy("system");
          paymentTransaction.setTransactionType(paymentTransactionModel.getTransactionType());
          paymentTransaction.setPaymentMethod(paymentTransactionModel.getPaymentMethod());


            return paymentTransaction;
        }

    public PaymentTransactionModel toModel(PaymentTransaction paymentTransaction) throws InvalidDateFormatException {
        PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
        paymentTransactionModel.setId(String.valueOf(paymentTransaction.getId()));
        paymentTransactionModel.setBankId(String.valueOf(paymentTransaction.getBankId()));
        paymentTransactionModel.setServiceId(String.valueOf(paymentTransaction.getServiceId()));
        paymentTransactionModel.setAmount(String.valueOf(paymentTransaction.getAmount()));
        paymentTransactionModel.setPaymentMethod(paymentTransaction.getPaymentMethod());
        paymentTransactionModel.setPaymentStatus(paymentTransaction.getPaymentStatus());
        paymentTransaction.setStatus(paymentTransaction.getStatus());
        return paymentTransactionModel;
    }

}
