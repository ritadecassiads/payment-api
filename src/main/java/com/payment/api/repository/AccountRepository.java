package com.payment.api.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.payment.api.model.Account;

@Repository
public class AccountRepository {
	private final Map<String, Account> accounts = new ConcurrentHashMap<>();
	
	public Account findById(String accountId) {
		return accounts.get(accountId);
	}
	
	public void saveAccount(Account account) {
		accounts.put(account.getAccountId(), account);
	}
	
	public void resetAccounts() {
		accounts.clear();
	}
	
}
