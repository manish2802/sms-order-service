package com.professionalit.exception;

public class WalletValidationException extends RuntimeException {

    public WalletValidationException(String message) {
        super(message);
    }
}