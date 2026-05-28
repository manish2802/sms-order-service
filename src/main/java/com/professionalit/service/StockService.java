package com.professionalit.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.professionalit.exception.StockNotFoundException;
import com.professionalit.model.StockDto;

@Service
public class StockService {

    private final RestClient restClient;
    private final String token;

    public StockService(
            RestClient.Builder restClientBuilder,
            @Value("${stock.services.finnhub.base-url}") String baseUrl,
            @Value("${stock.services.finnhub.token}") String token) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.token = token;
    }

    public StockDto getStock(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol is required");
        }

        String normalizedSymbol = symbol.trim().toUpperCase();
        FinnhubQuoteResponse quote = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", normalizedSymbol)
                        .queryParam("token", requireToken())
                        .build())
                .retrieve()
                .body(FinnhubQuoteResponse.class);

        if (quote == null || quote.c() == null || quote.c().compareTo(BigDecimal.ZERO) <= 0) {
            throw new StockNotFoundException(normalizedSymbol);
        }

        BigDecimal currentPrice = safeValue(quote.c());
        BigDecimal openingPrice = safeValue(quote.o());
        BigDecimal change = currentPrice.subtract(openingPrice);
        BigDecimal changePercent = BigDecimal.ZERO;
        if (openingPrice.compareTo(BigDecimal.ZERO) > 0) {
            changePercent = change.multiply(BigDecimal.valueOf(100))
                    .divide(openingPrice, 2, RoundingMode.HALF_UP);
        }

        return new StockDto(
                normalizedSymbol,
                normalizedSymbol,
                currentPrice,
                change,
                changePercent,
                safeValue(quote.h()),
                safeValue(quote.l()),
                0L);
    }

    private String requireToken() {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Finnhub API token is required");
        }
        return token;
    }

    private static BigDecimal safeValue(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static record FinnhubQuoteResponse(
            BigDecimal c,
            BigDecimal d,
            BigDecimal dp,
            BigDecimal h,
            BigDecimal l,
            BigDecimal o,
            BigDecimal pc,
            Long t) {
    }
}
