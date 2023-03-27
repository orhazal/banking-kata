package com.orhazal.bankingkata.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.domain.enums.OperationType;
import com.orhazal.bankingkata.repository.AccountRepository;
import com.orhazal.bankingkata.repository.OperationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImplementation implements AccountService {

	private AccountRepository accountRepository;
	private OperationRepository operationRepository;

	@Override
	public Operation processOperation(OperationType type, BigDecimal amount, Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Operation> getAccountHistory(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}