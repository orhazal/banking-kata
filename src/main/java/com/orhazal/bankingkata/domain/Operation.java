package com.orhazal.bankingkata.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.orhazal.bankingkata.enums.OperationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operation")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Operation {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "amount")
	private BigDecimal amount;

	// Named balance after to be clear that it is the balance after the operation is done
	@Column(name = "balance_after_operation", nullable = false)
	private BigDecimal balanceAfterOperation;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	@Column(name = "type", nullable = false)
	private OperationType type;

	@JoinColumn(name = "account", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
	@JsonBackReference // Jackson annotation to avoid infinite looping in the bidirectional relationship
	private Account account;

}