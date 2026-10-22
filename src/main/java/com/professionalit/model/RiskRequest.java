package com.professionalit.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskRequest {

    private String accountNumber;
    private String symbol;
    private Integer quantity;
    private BigDecimal price;
    private String type;
}
