package com.professionalit.exception;

public class RiskServiceUnavailableException extends RuntimeException {

    public RiskServiceUnavailableException(String message) {
        super(message);
    }
}