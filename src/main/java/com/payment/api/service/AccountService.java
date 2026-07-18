package com.payment.api.service;

import org.springframework.stereotype.Service;

import com.payment.api.model.Account;
import com.payment.api.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
	private AccountRepository accountRepository;
	
	public Integer getBalance(String accountId) {
		Account account = accountRepository.findById(accountId);
		
		return account == null ? null : account.getBalance();
	}
}
