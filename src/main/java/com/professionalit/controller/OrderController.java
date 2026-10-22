package com.professionalit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.professionalit.model.OrderRequest;
import com.professionalit.model.OrderResponse;
import com.professionalit.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {

		return ResponseEntity.ok(orderService.getOrder(orderId));
	}
}
