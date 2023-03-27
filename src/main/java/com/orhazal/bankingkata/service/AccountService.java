package com.orhazal.bankingkata.service;

import java.math.BigDecimal;
import java.util.List;

import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.enums.OperationType;

public interface AccountService {

	public Operation processOperation(OperationType type, BigDecimal amount, Long accountId);
	public List<Operation> getAccountHistory(Long accountId);

}