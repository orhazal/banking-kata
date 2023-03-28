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
import com.orhazal.bankingkata.service.utils.BigDecimalUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImplementation implements AccountService {

	private AccountRepository accountRepository;
	private OperationRepository operationRepository;

	@Override
	public Operation processOperation(OperationType type, BigDecimal amount, Long accountId) {
		// Looking for the account
		Account account = getAccountById(accountId);
		// Is the operation amount zero?
		if (BigDecimalUtils.isZero(amount)) {
			throw new NullAmountException("Cannot process a null amount for this operation");
		}
		// Get current balance from last operation
		BigDecimal currentBalance = getAccountBalance(account);
		// Calculate new balance
		BigDecimal balanceAfterOperation = OperationType.DEPOSIT.equals(type) ? currentBalance.add(amount) : currentBalance.subtract(amount);
		// Check if balance is negative
		if (BigDecimalUtils.isNegative(balanceAfterOperation)) {
			throw new NoEnoughFundsException(
					String.format("No enough funds to withdraw this amount (%s) from your current balance (%s)", amount, currentBalance));
		}
		// Result operation building
		Operation resultOperation = Operation.builder()
				.account(account)
				.amount(amount)
				.balanceAfterOperation(balanceAfterOperation)
				.timestamp(LocalDateTime.now())
				.type(type)
				.build();
		// Save operation
		return operationRepository.save(resultOperation);
	}

	@Override
	public List<Operation> getAccountHistory(Long accountId) {
		// Looking for the account
		Account account = getAccountById(accountId);
		return null;
	}

	private Account getAccountById(Long accountId) {
		Optional<Account> account = accountRepository.findById(accountId);
		if (!account.isPresent()) {
			throw new AccountNotFoundException("Account not found with accountId : " + accountId);
		}
		return account.get();
	}

	private BigDecimal getAccountBalance(Account account) {
		// Get account balance from last operation
		BigDecimal currentBalance;
		// If no operations in the account, the balance is zero
		if (account.getOperations() == null || account.getOperations().isEmpty()) {
			currentBalance = BigDecimal.ZERO;
		}
		else {
			// Get the current balance from the last operation on the account (comparing and getting last date)
			currentBalance = account.getOperations().stream().max(Comparator.comparing(Operation::getTimestamp)).get().getBalanceAfterOperation();
		}
		return currentBalance;
	}

}
