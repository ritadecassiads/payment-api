package com.payment.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payment.api.dto.EventRequestDTO;
import com.payment.api.dto.EventResponseDTO;
import com.payment.api.service.AccountService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	
	@GetMapping("/balance")
	public ResponseEntity<Integer> getBalance(@RequestParam("account_id") String accountId) {
		Integer balance = this.accountService.getBalance(accountId);
		
		if(balance == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
		} 
		
		return ResponseEntity.ok(balance);
	}
	
	@PostMapping("/reset") 
	public ResponseEntity<Void> resetAccounts() {
		 this.accountService.resetAccounts();
		 return ResponseEntity.ok().build();
	}
	
	@PostMapping("/event")
	public ResponseEntity<EventResponseDTO> processEvent(@RequestBody EventRequestDTO event) {
		EventResponseDTO response = accountService.processEvent(event);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);		
	}

}
