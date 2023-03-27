package com.orhazal.bankingkata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orhazal.bankingkata.domain.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}
