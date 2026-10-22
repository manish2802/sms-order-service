package com.professionalit.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private String orderId;

	private String accountNumber;

	private String symbol;

	private Integer quantity;

	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	private OrderType type; // BUY, SELL

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // PENDING, EXECUTED, FAILED

	private LocalDateTime createdDate;
}
