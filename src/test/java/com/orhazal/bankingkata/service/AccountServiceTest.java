package com.orhazal.bankingkata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.orhazal.bankingkata.domain.Account;
import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.enums.OperationType;
import com.orhazal.bankingkata.exceptions.AccountNotFoundException;
import com.orhazal.bankingkata.exceptions.NoEnoughFundsException;
import com.orhazal.bankingkata.exceptions.NullAmountException;
import com.orhazal.bankingkata.repository.AccountRepository;
import com.orhazal.bankingkata.repository.OperationRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;
	@Mock
	private OperationRepository operationRepository;
	@InjectMocks
	private AccountServiceImplementation accountServiceImplementation;

	private static Account account;

	@BeforeAll
	public static void setup() {
		account = Account.builder().id(1L).build();
	}

	@DisplayName("When processing an operation, the accountId should return an existing account")
	@Test
	public void givenInvalidAccountId_whenProcessOperation_thenThrowsException() {
		Long invalidAccountId = 10L;
		when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());
		assertThrows(AccountNotFoundException.class, 
				() -> { accountServiceImplementation.processOperation(OperationType.DEPOSIT, BigDecimal.TEN, invalidAccountId); });
	}

	@DisplayName("When processing an operation, the amount shouldn't be zero")
	@Test
	public void givenNullAmount_whenProcessOperation_thenThrowsException() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		assertThrows(NullAmountException.class, 
				() -> { accountServiceImplementation.processOperation(OperationType.DEPOSIT, BigDecimal.ZERO, 1L); });
	}

	@DisplayName("When processing an operation, the account balance should be zero when no operations")
	@Test
	public void givenNoOperations_whenProcessOperation_thenCurrentBalanceIsZero() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(operationRepository.save(Mockito.any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());
		Operation operation = accountServiceImplementation.processOperation(OperationType.DEPOSIT, BigDecimal.TEN, 1L);
		assertThat(BigDecimal.TEN.compareTo(operation.getBalanceAfterOperation()) == 0);
	}

	@DisplayName("When making deposits, balance amounts should add up")
	@Test
	public void givenDeposits_whenProcessOperation_thenAmountsAddedToBalance() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(operationRepository.save(Mockito.any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());
		Operation firstOperation = accountServiceImplementation.processOperation(OperationType.DEPOSIT, new BigDecimal(5), 1L);
		when(accountRepository.findById(1L)).thenReturn(Optional.of(Account.builder().id(1L).operations(List.of(firstOperation)).build()));
		Operation secondOperation = accountServiceImplementation.processOperation(OperationType.DEPOSIT, new BigDecimal(10), 1L);
		assertThat(new BigDecimal(15).compareTo(secondOperation.getBalanceAfterOperation()) == 0);
	}

	@DisplayName("When withdrawing, account should have enough balance")
	@Test
	public void givenWithdraw_whenProcessOperation_thenBalanceShouldBePositive() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		when(operationRepository.save(Mockito.any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());
		Operation firstOperation = accountServiceImplementation.processOperation(OperationType.DEPOSIT, new BigDecimal(100), 1L);
		when(accountRepository.findById(1L)).thenReturn(Optional.of(Account.builder().id(1L).operations(List.of(firstOperation)).build()));
		assertThrows(NoEnoughFundsException.class, 
				() -> { accountServiceImplementation.processOperation(OperationType.WITHDRAWAL, new BigDecimal(200), 1L); });
	}

	@DisplayName("When checking history, the accountId should return an existing account")
	@Test
	public void givenInvalidAccountId_whenGetAccountHistory_thenThrowsException() {
		Long invalidAccountId = 10L;
		when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());
		assertThrows(AccountNotFoundException.class, 
				() -> { accountServiceImplementation.getAccountHistory(invalidAccountId); });
	}

	@DisplayName("When checking history, a new account should return an empty operation list")
	@Test
	public void givenNoOperations_whenGetAccountHistory_thenReturnEmptyOperationList() {
		when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
		List<Operation> operationList = accountServiceImplementation.getAccountHistory(1L);
		assertTrue(operationList.isEmpty());
	}
}
