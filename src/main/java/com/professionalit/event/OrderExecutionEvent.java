package com.professionalit.event;

import java.math.BigDecimal;

import com.professionalit.entity.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderExecutionEvent {

    private String orderId;

    private String accountNumber;

    private String symbol;

    private Integer quantity;

    private BigDecimal price;

    private OrderType type;
}
