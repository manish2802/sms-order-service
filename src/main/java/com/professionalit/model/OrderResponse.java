package com.professionalit.model;

import java.math.BigDecimal;

import com.professionalit.entity.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private String orderId;
    private String accountNumber;
    private String symbol;
    private Integer quantity;
    private BigDecimal price;
    private String type;
    private OrderStatus status;
}