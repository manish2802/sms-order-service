package com.professionalit.service;


import org.springframework.stereotype.Service;

import com.professionalit.client.RiskServiceClient;
import com.professionalit.exception.RiskServiceUnavailableException;
import com.professionalit.model.RiskRequest;
import com.professionalit.model.RiskResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiskServiceProxy {

    private final RiskServiceClient riskServiceClient;

    @CircuitBreaker(
        name = "riskService",
        fallbackMethod = "riskFallback"
    )
    public RiskResponse validateRisk(RiskRequest request) {
        return riskServiceClient.validateRisk(request);
    }

    public RiskResponse riskFallback(
            RiskRequest request,
            Throwable throwable) {

        throw new RiskServiceUnavailableException(
            "Risk Service is unavailable. Please try again later."
        );
    }
}