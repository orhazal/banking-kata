package com.orhazal.bankingkata.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.orhazal.bankingkata.domain.Account;
import com.orhazal.bankingkata.domain.Operation;
import com.orhazal.bankingkata.enums.OperationType;
import com.orhazal.bankingkata.exceptions.AccountNotFoundException;
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
	private static Operation depositOperation;
	private static Operation withdrawalOperation;
	private static Operation negativeAmountOperation;
	private static Operation zeroAmountOperation;

	@BeforeAll
	public static void setupFakeData() {
		account = Account.builder().id(1L).build();
		depositOperation = Operation.builder().id(1L).
				account(account).
				amount(new BigDecimal(1000.15)).
				type(OperationType.DEPOSIT).
				balanceAfter(new BigDecimal(1000.15)).
				timestamp(LocalDateTime.now()).build();
		withdrawalOperation = Operation.builder().id(2L).
				account(account).
				amount(new BigDecimal(500.05)).
				type(OperationType.WITHDRAWAL).
				balanceAfter(new BigDecimal(500.1)).
				timestamp(LocalDateTime.now().plus(Period.ofDays(1))).build();
		negativeAmountOperation = Operation.builder().id(3L).
				account(account).
				amount(new BigDecimal(5000.99)).
				type(OperationType.WITHDRAWAL).
				balanceAfter(new BigDecimal(-4500.89)).
				timestamp(LocalDateTime.now().plus(Period.ofDays(2))).build();
		zeroAmountOperation = Operation.builder().id(4L).
				account(account).
				amount(new BigDecimal(0)).
				type(OperationType.DEPOSIT).
				balanceAfter(new BigDecimal(0)).
				timestamp(LocalDateTime.now()).build();
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
	
}
