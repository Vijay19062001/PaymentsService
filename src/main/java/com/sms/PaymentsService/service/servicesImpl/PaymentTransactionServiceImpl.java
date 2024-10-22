package com.sms.PaymentsService.service.servicesImpl;

import com.sms.PaymentsService.entity.Bank;
import com.sms.PaymentsService.entity.PaymentTransaction;
import com.sms.PaymentsService.enums.PaymentStatus;
import com.sms.PaymentsService.enums.Status;
import com.sms.PaymentsService.exception.custom.InvalidDateFormatException;
import com.sms.PaymentsService.mapper.PaymentTransactionMapper;
import com.sms.PaymentsService.model.PaymentTransactionModel;
import com.sms.PaymentsService.repository.BankRepository;
import com.sms.PaymentsService.repository.PaymentTransactionRepository;
import com.sms.PaymentsService.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final BankRepository bankRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;

    @Transactional
    @Override
    public PaymentTransactionModel processPayment(PaymentTransactionModel paymentTransactionModel, String authToken) throws InvalidDateFormatException {
        logger.info("Processing payment for subscription ID: {}", paymentTransactionModel.getSubscriptionId());

        validatePaymentDetails(paymentTransactionModel);
        validateAuthToken(authToken);

        Optional<Bank> bankOptional = bankRepository.findById(Integer.valueOf(paymentTransactionModel.getBankId()));
        if (bankOptional.isEmpty()) {
            logger.error("Bank account with ID {} not found.", paymentTransactionModel.getBankId());
            throw new RuntimeException("Bank account not found.");
        }

        Bank bank = bankOptional.get();
        if (bank.getBalance() < Double.parseDouble(paymentTransactionModel.getAmount())) {
            logger.error("Insufficient funds in bank account ID: {}. Available balance: {}", bank.getId(), bank.getBalance());
            throw new RuntimeException("Insufficient funds.");
        }

        try {
            bank.setBalance(bank.getBalance() - Double.parseDouble(paymentTransactionModel.getAmount()));
            bank.setUpdatedDate(LocalDateTime.now());
            bankRepository.save(bank);

            PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionModel);
            paymentTransaction.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentTransaction.setStatus(Status.ACTIVE);
            paymentTransaction.setCreatedDate(LocalDateTime.now());
            paymentTransaction.setUpdatedDate(LocalDateTime.now());
            paymentTransaction.setCreatedBy("system");
            paymentTransaction.setUpdatedBy("system");

            paymentTransactionRepository.save(paymentTransaction);
            logger.info("Payment transaction successfully completed with ID: {}", paymentTransaction.getId());

            paymentTransactionModel = paymentTransactionMapper.toModel(paymentTransaction);

        } catch (Exception e) {
            logger.error("Error occurred while processing payment transaction: {}", e.getMessage(), e);
            paymentTransactionModel.setPaymentStatus(PaymentStatus.FAILED);
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }

        return paymentTransactionModel;
    }

    private void validatePaymentDetails(PaymentTransactionModel paymentTransactionModel) {
        logger.info("Validating payment details for subscription ID: {}", paymentTransactionModel.getSubscriptionId());

        if (paymentTransactionModel.getAmount() == null || paymentTransactionModel.getAmount().isEmpty()) {
            logger.error("Payment amount is missing.");
            throw new IllegalArgumentException("Payment amount is required.");
        }

        try {
            double amount = Double.parseDouble(paymentTransactionModel.getAmount().trim());
            if (amount <= 0) {
                logger.error("Invalid payment amount: {}", paymentTransactionModel.getAmount());
                throw new IllegalArgumentException("Invalid payment amount.");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid format for payment amount: {}", paymentTransactionModel.getAmount());
            throw new IllegalArgumentException("Payment amount must be a valid number.");
        }

        if (paymentTransactionModel.getBankId() == null || paymentTransactionModel.getBankId().isEmpty()) {
            logger.error("Bank ID is missing.");
            throw new IllegalArgumentException("Bank ID is required.");
        }

        if (paymentTransactionModel.getSubscriptionId() == null || paymentTransactionModel.getSubscriptionId().isEmpty()) {
            logger.error("Subscription ID is missing.");
            throw new IllegalArgumentException("Subscription ID is required.");
        }
    }

    private void validateAuthToken(String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            logger.error("Authorization token is missing or invalid.");
            throw new RuntimeException("Authorization token is missing or invalid.");
        }
        logger.info("Authorization token validation successful.");
    }
}