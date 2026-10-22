package com.professionalit.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.professionalit.model.RiskRequest;
import com.professionalit.model.RiskResponse;



@FeignClient(name = "sms-risk-service")
public interface RiskServiceClient {

    @PostMapping("/api/risk/validate")
    RiskResponse validateRisk(@RequestBody RiskRequest request);
}