package com.professionalit.service;

import org.springframework.stereotype.Service;

import com.professionalit.client.WalletGrpcClient;
import com.professionalit.exception.WalletServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceProxy {

    private final WalletGrpcClient walletGrpcClient;

    @CircuitBreaker(
        name = "walletService",
        fallbackMethod = "walletFallback"
    )
    public boolean validateWallet(
            String accountNumber,
            long orderValue) {

        return walletGrpcClient.validateBalance(
            accountNumber,
            orderValue
        );
    }

    public boolean walletFallback(
            String accountNumber,
            long orderValue,
            Throwable throwable) {

        throw new WalletServiceUnavailableException(
            "Wallet Service is unavailable. Please try again later."
        );
    }
}