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
	private AccountRepository accountRepository;
	
	public Integer getBalance(String accountId) {
		Account account = accountRepository.findById(accountId);
		
		return account == null ? null : account.getBalance();
	}
	
	public void resetAccounts() {
		accountRepository.resetAccounts();
	}
	
	public EventResponseDTO processEvent(EventRequestDTO eventRequest) {
		switch (eventRequest.getType()) {
			case "deposit":
				return processDeposit(eventRequest);
//			case "withdraw":
//				processWithdraw(eventRequest);
//				break;
//			case "transfer":
//				processTransfer(eventRequest);
//				break;
			default:
				throw new IllegalArgumentException("Invalid event type: " + eventRequest.getType());
		}
	}
	
	private EventResponseDTO processDeposit(EventRequestDTO eventRequest) {
		Account account = accountRepository.findById(eventRequest.getDestination());
		
		if(account == null) {
			account = new Account(eventRequest.getDestination(), eventRequest.getAmount());
			System.out.println("Criando uma nova conta...");
		} else {
			account.setBalance(account.getBalance() + eventRequest.getAmount());			
		}
		
		accountRepository.saveAccount(account);
		
		return new EventResponseDTO(null, account);
	}
	
	private void processWithdraw(EventRequestDTO eventRequest) {
		// TODO Auto-generated method stub
		
	}


	private void processTransfer(EventRequestDTO eventRequest) {
		
	}


	
}
