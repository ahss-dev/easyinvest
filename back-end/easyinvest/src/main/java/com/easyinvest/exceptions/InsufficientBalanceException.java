package com.easyinvest.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class InsufficientBalanceException extends RuntimeException {

            public InsufficientBalanceException(String message) {
                super(message);
            }
    }
