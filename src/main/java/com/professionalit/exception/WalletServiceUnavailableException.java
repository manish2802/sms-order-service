package com.professionalit.exception;

public class WalletServiceUnavailableException extends RuntimeException {

	public WalletServiceUnavailableException(String message) {
		super(message);
	}
}