package com.orhazal.bankingkata.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orhazal.bankingkata.domain.Account;
import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.enums.OperationType;
import com.orhazal.bankingkata.repository.AccountRepository;
import com.orhazal.bankingkata.service.AccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AccountController {

	private AccountService accountService;
	private AccountRepository accountRepository;

	@PostMapping(path = "/v1/accounts/{id}/operations")
	public ResponseEntity<Operation> processOperation(@PathVariable(name = "id") Long accountId, @RequestParam OperationType type,
			@RequestParam Double amount) {
		return new ResponseEntity<>(accountService.processOperation(type, new BigDecimal(amount), accountId), HttpStatus.CREATED);
	}

	@GetMapping(path = "/v1/accounts/{id}/operations")
	public ResponseEntity<List<Operation>> getAccountHistory(@PathVariable(name = "id") Long accountId) {
		return ResponseEntity.ok(accountService.getAccountHistory(accountId));
	}

	/*
	 * Debug and test endpoints
	 */
	@GetMapping(path = "/v1/accounts/{id}")
	public ResponseEntity<Account> getAccount(@PathVariable(name = "id") Long accountId) {
		return ResponseEntity.ok(accountRepository.findById(accountId).orElse(null));
	}

	@PostMapping(path = "/v1/accounts/")
	public ResponseEntity<Account> createAccount() {
		return new ResponseEntity<>(accountRepository.save(new Account()), HttpStatus.CREATED);
	}
	
}
