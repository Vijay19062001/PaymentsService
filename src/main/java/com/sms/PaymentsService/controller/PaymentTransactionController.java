package com.sms.PaymentsService.controller;

import com.sms.PaymentsService.exception.custom.BusinessValidationException;
import com.sms.PaymentsService.model.PaymentTransactionModel;
import com.sms.PaymentsService.service.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "PaymentTransaction")
public class PaymentTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTransactionController.class);
    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/transaction")
    @Operation(summary = "Create PaymentTransaction",
            description = "Creates a new PaymentTransaction with the provided details.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "PaymentTransaction created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Invalid or missing token")
    })
    public ResponseEntity<String> addPaymentTransaction(
            @RequestBody @Valid PaymentTransactionModel paymentTransactionModel) {

        logger.info("Received request to create payment transaction with details: {}", paymentTransactionModel);

        try {
            PaymentTransactionModel processedTransaction = paymentTransactionService.processPayment(paymentTransactionModel);
            String successMessage = "Payment transaction successfully added with ID: " + processedTransaction.getId();
            logger.info(successMessage);
            return ResponseEntity.status(201).body(successMessage);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error while creating payment transaction: {}", e.getMessage());
            return ResponseEntity.badRequest().body(" error: " + e.getMessage());

        } catch (BusinessValidationException e) {
            logger.error("Business validation error while processing payment: {}", e.getMessage());
            return ResponseEntity.status(422).body("Business error: " + e.getMessage());

        } catch (Exception e) {
            logger.error("error occurred: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
    }
}
