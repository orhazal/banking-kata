package com.orhazal.bankingkata.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Account {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	// TODO : thought about lastBalanceMovement (LocalTime) and lastBalanceOperation (Operation) columns but it is duplicated info, can be checked in Operation table

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	private Set<Operation> operations;

}