package com.professionalit.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.professionalit.config.RateLimitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private final RateLimitConfig rateLimitConfig;
	private final ObjectMapper objectMapper;

	private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

	public RateLimitFilter(RateLimitConfig rateLimitConfig, ObjectMapper objectMapper) {
		this.rateLimitConfig = rateLimitConfig;
		this.objectMapper = objectMapper;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !("POST".equalsIgnoreCase(request.getMethod()) && "/api/v1/orders".equals(request.getRequestURI()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Demo only: derive the key from the authenticated identity
		// in production, not from an untrusted request body/header.
		String accountNumber = request.getUserPrincipal() != null ? request.getUserPrincipal().getName()
				: request.getRemoteAddr();

		Bucket bucket = buckets.computeIfAbsent(accountNumber,
				key -> Bucket.builder().addLimit(rateLimitConfig.orderRateLimitBandwidth()).build());

		if (bucket.tryConsume(1)) {
			filterChain.doFilter(request, response);
			return;
		}

		response.setStatus(429);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		objectMapper.writeValue(response.getWriter(), java.util.Map.of("code", "RATE_LIMIT_EXCEEDED", "message",
				"Order request limit exceeded. Please try again later."));
	}
}