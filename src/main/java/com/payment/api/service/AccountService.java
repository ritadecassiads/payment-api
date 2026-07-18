package com.payment.api.service;

import org.springframework.stereotype.Service;

import com.payment.api.dto.EventRequestDTO;
import com.payment.api.dto.EventResponseDTO;
import com.payment.api.model.Account;
import com.payment.api.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;

	public synchronized Integer getBalance(String accountId) {
		Account account = accountRepository.findById(accountId);

		return account == null ? null : account.getBalance();
	}

	public synchronized  void resetAccounts() {
		accountRepository.resetAccounts();
	}

	public synchronized EventResponseDTO processEvent(EventRequestDTO eventRequest) {
		return switch (eventRequest.getType()) {
		case "deposit" -> processDeposit(eventRequest);
		case "withdraw" -> processWithdraw(eventRequest);
		case "transfer" -> processTransfer(eventRequest);
		default -> throw new IllegalArgumentException("Invalid event type");
		};
	}

	private EventResponseDTO processDeposit(EventRequestDTO eventRequest) {
		Account account = accountRepository.findById(eventRequest.getDestination());

		if (account == null) {
			account = new Account(eventRequest.getDestination(), eventRequest.getAmount());
		} else {
			account.setBalance(account.getBalance() + eventRequest.getAmount());
		}

		accountRepository.saveAccount(account);

		return new EventResponseDTO(null, account);
	}

	private EventResponseDTO processWithdraw(EventRequestDTO eventRequest) {
		Account account = accountRepository.findById(eventRequest.getOrigin());

		if (account == null) {
			return null;
		}

		if (account.getBalance() < eventRequest.getAmount()) {
			throw new IllegalStateException("Insufficient balance");
		}

		account.setBalance(account.getBalance() - eventRequest.getAmount());

		accountRepository.saveAccount(account);

		return new EventResponseDTO(account, null);
	}

	private EventResponseDTO processTransfer(EventRequestDTO eventRequest) {
		Account origin = accountRepository.findById(eventRequest.getOrigin());
		
		if (origin == null) {
			return null;
		}

		if (origin.getBalance() < eventRequest.getAmount()) {
			throw new IllegalStateException("Insufficient balance");
		}

		Account destination = accountRepository.findById(eventRequest.getDestination());
		
		if (destination == null) {
			destination = new Account(eventRequest.getDestination(), 0);
		}

		origin.setBalance(origin.getBalance() - eventRequest.getAmount());
		destination.setBalance(destination.getBalance() + eventRequest.getAmount());

		accountRepository.saveAccount(origin);
		accountRepository.saveAccount(destination);

		return new EventResponseDTO(origin, destination);
	}

}
