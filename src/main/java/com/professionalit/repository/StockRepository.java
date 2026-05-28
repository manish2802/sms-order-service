package com.professionalit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.professionalit.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByStockSymbol(String symbol);

    boolean existsByStockSymbol(String symbol);
}
