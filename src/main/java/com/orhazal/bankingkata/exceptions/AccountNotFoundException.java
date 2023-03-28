package com.orhazal.bankingkata.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Extends RuntimeException because it is an unchecked exception and cannot be detected at compile time
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Account not found")
public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String message) {
		super(message);
	}
	
}
