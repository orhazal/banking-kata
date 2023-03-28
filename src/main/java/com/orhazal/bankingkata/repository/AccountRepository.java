package com.orhazal.bankingkata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orhazal.bankingkata.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
