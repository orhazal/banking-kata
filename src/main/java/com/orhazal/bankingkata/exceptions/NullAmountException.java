package com.orhazal.bankingkata.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Extends RuntimeException because it is an unchecked exception and cannot be detected at compile time
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Cannot deposit/withdrawal with a null amount")
public class NullAmountException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NullAmountException(String message) {
		super(message);
	}
	
}