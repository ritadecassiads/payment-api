package com.payment.api.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.payment.api.model.Account;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
	private final Map<String, Account> accounts = new ConcurrentHashMap<>();
	
	/**
	 *
	 * Note: this repository is an in-memory implementation backed by a
	 * ConcurrentHashMap. It returns null when an account is not found — this
	 * choice keeps the API simple for the exercise. If the project migrates to
	 * Spring Data JPA, consider returning Optional<Account> instead.
	 *
	 */
	public Account findById(String accountId) {
		return accounts.get(accountId);
	}
	
	public void saveAccount(Account account) {
		accounts.put(account.getId(), account);
	}
	
	public void resetAccounts() {
		accounts.clear();
	}
	
}
