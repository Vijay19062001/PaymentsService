package com.sms.PaymentsService.exception;

import com.sms.PaymentsService.exception.constants.ErrorResponse;
import com.sms.PaymentsService.exception.custom.BasicValidationException;
import com.sms.PaymentsService.exception.custom.InvalidPaymentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = new ErrorResponse() {
            @Override
            public String getMessage()
            {
                return ex.getMessage();
            }

            @Override
            public int getStatus() {
                return HttpStatus.BAD_REQUEST.value();

            }
        };

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BasicValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBasicValidationException(BasicValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse() {
            @Override
            public String getMessage()
            {
                return ex.getMessage();
            }

            @Override
            public int getStatus() {
                return HttpStatus.BAD_REQUEST.value();

            }
        };
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<String> handleInvalidPayment(InvalidPaymentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
