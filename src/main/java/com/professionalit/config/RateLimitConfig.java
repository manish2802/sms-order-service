package com.professionalit.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimitConfig {

	@Bean
	public Bandwidth orderRateLimitBandwidth() {
		return Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(10)));
	}
}