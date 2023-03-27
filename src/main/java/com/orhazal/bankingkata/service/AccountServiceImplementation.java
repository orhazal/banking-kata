package com.orhazal.bankingkata.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.orhazal.bankingkata.domain.Account;
import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.enums.OperationType;
import com.orhazal.bankingkata.exceptions.AccountNotFoundException;
import com.orhazal.bankingkata.exceptions.NoEnoughFundsException;
import com.orhazal.bankingkata.exceptions.NullAmountException;
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
		// Check for account
		Optional<Account> account = accountRepository.findById(accountId);
		if (!account.isPresent()) {
			throw new AccountNotFoundException("Account not found with accountId : " + accountId);
		}
		// Is the operation amount zero?
		if (BigDecimal.ZERO.compareTo(amount) == 0) {
			throw new NullAmountException("Cannot process a null amount for this operation");
		}
		// Get current balance from last operation
		BigDecimal currentBalance;
		if (account.get().getOperations() == null || account.get().getOperations().isEmpty()) {
			currentBalance = BigDecimal.ZERO;
		}
		else {
			currentBalance = account.get().getOperations().stream().max(Comparator.comparing(Operation::getTimestamp)).get().getBalanceAfterOperation();
		}
		// Calculate new balance
		BigDecimal balanceAfterOperation = OperationType.DEPOSIT.equals(type) ? currentBalance.add(amount) : currentBalance.subtract(amount);
		// Check if balance is negative
		if (balanceAfterOperation.signum() == -1) {
			throw new NoEnoughFundsException(
					String.format("No enough funds to withdraw this amount (%s) from your current balance (%s)", amount, currentBalance));
		}
		// Result operation building
		Operation resultOperation = Operation.builder()
				.account(account.get())
				.amount(amount)
				.balanceAfterOperation(balanceAfterOperation)
				.timestamp(LocalDateTime.now())
				.type(type)
				.build();
		return resultOperation;
	}

	@Override
	public List<Operation> getAccountHistory(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
