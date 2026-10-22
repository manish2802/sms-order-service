package com.professionalit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.professionalit.entity.Order;
import com.professionalit.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	 Optional<Order> findByOrderId(String orderId);

	    List<Order> findByAccountNumber(String accountNumber);

	    List<Order> findByStatus(OrderStatus status);
}
