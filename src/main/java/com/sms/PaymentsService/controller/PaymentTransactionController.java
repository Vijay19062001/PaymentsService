package com.sms.PaymentsService.controller;

import com.sms.PaymentsService.model.PaymentTransactionModel;
import com.sms.PaymentsService.service.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "PaymentTransaction")
public class PaymentTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTransactionController.class);
    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/add")
    @Operation(summary = "Create PaymentTransaction",
            description = "Creates a new PaymentTransaction with the provided details.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "PaymentTransaction created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Unauthorized: Invalid or missing token")
    })
    public ResponseEntity<String> addPaymentTransaction(
            @RequestBody @Valid PaymentTransactionModel paymentTransactionModel,
            @RequestHeader("Authorization") String authToken) {

        logger.info("Received request to create payment transaction with details: {}", paymentTransactionModel);

        try {
            PaymentTransactionModel processedTransaction = paymentTransactionService.processPayment(paymentTransactionModel, authToken);
            String successMessage = "Payment transaction successfully added with ID: " + processedTransaction.getId();
            logger.info("Payment transaction created successfully: {}", processedTransaction.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while creating payment transaction: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error processing payment transaction: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment: " + e.getMessage());
        }
    }
}
