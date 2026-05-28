package com.professionalit.model;

import java.math.BigDecimal;

public record StockDto(
        String symbol,
        String companyName,
        BigDecimal lastPrice,
        BigDecimal change,
        BigDecimal changePercent,
        BigDecimal dayHigh,
        BigDecimal dayLow,
        long volume) {
}
