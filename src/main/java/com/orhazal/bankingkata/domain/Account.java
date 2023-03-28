package com.orhazal.bankingkata.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Operation> operations;

}