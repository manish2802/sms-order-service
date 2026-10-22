package com.professionalit.model;

import java.math.BigDecimal;

import com.professionalit.entity.OrderType;

import lombok.Data;

@Data
public class OrderRequest {
    private String accountNumber;
    private String symbol;
    private Integer quantity;
    private BigDecimal price;
    private OrderType type;
}