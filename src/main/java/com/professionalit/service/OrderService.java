package com.professionalit.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.professionalit.entity.Order;
import com.professionalit.entity.OrderStatus;
import com.professionalit.event.OrderExecutionEvent;
import com.professionalit.exception.RiskValidationException;
import com.professionalit.exception.WalletValidationException;
import com.professionalit.model.OrderRequest;
import com.professionalit.model.OrderResponse;
import com.professionalit.model.RiskRequest;
import com.professionalit.model.RiskResponse;
import com.professionalit.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderExecutionEvent> kafkaTemplate;

    private final WalletServiceProxy walletServiceProxy;
    private final RiskServiceProxy riskServiceProxy;

    // =========================================================
    // PLACE ORDER
    // =========================================================

    public OrderResponse placeOrder(OrderRequest request) {

        // 1. Calculate order value
        long orderValue = request.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()))
                .longValueExact();

        // 2. Wallet validation - SYNC gRPC + CIRCUIT BREAKER
        boolean walletApproved = walletServiceProxy.validateWallet(
                request.getAccountNumber(),
                orderValue
        );

        if (!walletApproved) {
            throw new WalletValidationException(
                    "Insufficient wallet balance for account: "
                            + request.getAccountNumber()
            );
        }

        // 3. Prepare risk request
        RiskRequest riskRequest = RiskRequest.builder()
                .accountNumber(request.getAccountNumber())
                .symbol(request.getSymbol())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .type(request.getType().name())
                .build();

        // 4. Risk validation - SYNC Feign + CIRCUIT BREAKER
        RiskResponse riskResponse =
                riskServiceProxy.validateRisk(riskRequest);

        if (!riskResponse.isApproved()) {
            throw new RiskValidationException(
                    "Order rejected by risk service: "
                            + riskResponse.getMessage()
            );
        }

        // 5. Create order
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .accountNumber(request.getAccountNumber())
                .symbol(request.getSymbol())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .type(request.getType())
                .status(OrderStatus.PENDING)
                .createdDate(LocalDateTime.now())
                .build();

        // 6. Save order
        orderRepository.save(order);

        // 7. Publish order execution event asynchronously
        OrderExecutionEvent event = OrderExecutionEvent.builder()
                .orderId(order.getOrderId())
                .accountNumber(order.getAccountNumber())
                .symbol(order.getSymbol())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .type(order.getType())
                .build();

        kafkaTemplate.send(
                "order.execution",
                order.getSymbol(),
                event
        );

        // 8. Return response
        return mapToResponse(order);
    }

    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    public OrderResponse getOrder(String orderId) {

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + orderId
                        )
                );

        return mapToResponse(order);
    }

    // =========================================================
    // ENTITY TO RESPONSE MAPPING
    // =========================================================

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .accountNumber(order.getAccountNumber())
                .symbol(order.getSymbol())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .type(order.getType().name())
                .status(order.getStatus())
                .build();
    }
}