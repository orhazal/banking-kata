package com.orhazal.bankingkata.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.orhazal.bankingkata.domain.enums.OperationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operation")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Operation {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	// Named balance after to be clear that it is the balance after the operation is done
	@Column(name = "balance_after", nullable = false)
	private BigDecimal balanceAfter;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	@Column(name = "type", nullable = false)
	private OperationType type;

	@JoinColumn(name = "account", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
	private Account account;

}