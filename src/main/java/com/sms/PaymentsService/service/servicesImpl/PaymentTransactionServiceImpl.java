package com.sms.PaymentsService.service.servicesImpl;

import com.sms.PaymentsService.entity.Bank;
import com.sms.PaymentsService.entity.PaymentTransaction;
import com.sms.PaymentsService.enums.PaymentStatus;
import com.sms.PaymentsService.enums.TransactionType;
import com.sms.PaymentsService.exception.custom.BasicValidationException;
import com.sms.PaymentsService.exception.custom.BusinessValidationException;
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
    public PaymentTransactionModel processPayment(PaymentTransactionModel paymentTransactionModel) throws InvalidDateFormatException {
        logger.info("Processing payment for service ID: {}", paymentTransactionModel.getServiceId());

        validatePaymentDetails(paymentTransactionModel);

        Optional<Bank> bankOptional = bankRepository.findByAccountNumber(paymentTransactionModel.getAccountNumber());
        if (bankOptional.isEmpty()) {
            logger.error("Bank account with ID {} not found.", paymentTransactionModel.getAccountNumber());
            throw new BusinessValidationException("Bank account not found.");
        }

        Bank bank = bankOptional.get();

        double transactionAmount = Double.parseDouble(paymentTransactionModel.getAmount());
        if (bank.getBalance() < transactionAmount && paymentTransactionModel.getTransactionType() == TransactionType.DEBITCARD) {
            logger.error("Insufficient funds in bank account ID: {}. Available balance: {}", bank.getId(), bank.getBalance());
            throw new BusinessValidationException("Insufficient funds.");
        }

        try {
            updateBankAccountBalance(bank, paymentTransactionModel);

            PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionModel);


            paymentTransactionRepository.save(paymentTransaction);
            logger.info("Payment transaction successfully completed with ID: {}", paymentTransaction.getId());

            paymentTransactionModel = paymentTransactionMapper.toModel(paymentTransaction);

        } catch (Exception e) {
            logger.error("Error processing payment transaction: {}", e.getMessage(), e);
            paymentTransactionModel.setPaymentStatus(PaymentStatus.FAILED);
            throw new BusinessValidationException("Payment failed: " + e.getMessage());
        }

        return paymentTransactionModel;
    }

    private void validatePaymentDetails(PaymentTransactionModel paymentTransactionModel) {
        logger.info("Validating payment details for service ID: {}", paymentTransactionModel.getServiceId());

        if (paymentTransactionModel.getAmount() == null || paymentTransactionModel.getAmount().isEmpty()) {
            logger.error("Payment amount is missing.");
            throw new BasicValidationException("Payment amount is required.");
        }
        
        try {
            double amount = Double.parseDouble(paymentTransactionModel.getAmount().trim());
            if (amount <= 0) {
                logger.error("Invalid payment amount: {}", paymentTransactionModel.getAmount());
                throw new BasicValidationException("Payment amount must be positive.");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid format for payment amount: {}", paymentTransactionModel.getAmount());
            throw new BasicValidationException("Payment amount must be a valid number.");
        }

        if (paymentTransactionModel.getAccountNumber() == null || paymentTransactionModel.getAccountNumber().isEmpty()) {
            logger.error("Bank ID is missing.");
            throw new BasicValidationException("Bank ID is required.");
        }

        if (paymentTransactionModel.getServiceId() == null || paymentTransactionModel.getServiceId().isEmpty()) {
            logger.error("Service ID is missing.");
            throw new BasicValidationException("Service ID is required.");
        }
    }

    public void updateBankAccountBalance(Bank bankAccount, PaymentTransactionModel model) {
        logger.info("Updating bank account balance for account ID: {}", bankAccount.getAccountNumber());

        double amount = Double.parseDouble(model.getAmount());

        if (TransactionType.DEBITCARD.equals(model.getTransactionType())) {
            if (bankAccount.getBalance() < amount) {
                logger.error("Insufficient balance in the account. Available balance: {}, Required amount: {}", bankAccount.getBalance(), amount);
                throw new BusinessValidationException("Insufficient balance in the account.");
            }
            bankAccount.setBalance(bankAccount.getBalance() - amount);
        } else if (TransactionType.CREDITCARD.equals(model.getTransactionType())) {
            bankAccount.setBalance(bankAccount.getBalance() + amount);
        }

        bankAccount.setUpdatedDate(LocalDateTime.now());
        bankAccount.setUpdatedBy(model.getUserId());

        bankRepository.save(bankAccount);
        logger.info("Bank account balance updated successfully for account ID: {}", bankAccount.getAccountNumber());
    }
}