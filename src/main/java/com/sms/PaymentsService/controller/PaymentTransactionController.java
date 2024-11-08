package com.sms.PaymentsService.controller;

import com.sms.PaymentsService.model.PaymentTransactionModel;
import com.sms.PaymentsService.service.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "PaymentTransaction")
public class PaymentTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTransactionController.class);

    @Autowired
    private final PaymentTransactionService paymentTransactionService;

    @PostMapping("/transaction")
    @Operation(summary = "Create PaymentTransaction",
            description = "Creates a new PaymentTransaction with the provided details.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "PaymentTransaction created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Invalid or missing token")
    })

    public ResponseEntity<?> addPaymentTransaction(@RequestBody PaymentTransactionModel paymentTransactionModel) {
        logger.info("Received request to create payment transaction with details: {}", paymentTransactionModel);

            return ResponseEntity.ok(paymentTransactionService.processPayment(paymentTransactionModel));
    }


}
