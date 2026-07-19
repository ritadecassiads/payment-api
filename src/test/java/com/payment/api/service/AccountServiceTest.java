package com.payment.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.payment.api.dto.EventRequestDTO;
import com.payment.api.dto.EventResponseDTO;
import com.payment.api.exception.InsufficientBalanceException;
import com.payment.api.model.Account;
import com.payment.api.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService;

	@Test
	void shouldReturnAccountBalance() {
		Account account = new Account("100", 25);

		when(accountRepository.findById("100")).thenReturn(account);

		Long balance = accountService.getBalance("100");

		assertEquals(25, balance);
	}

	@Test
	void shouldReturnNullWhenAccountDoesNotExist() {
		when(accountRepository.findById("999")).thenReturn(null);

		Long balance = accountService.getBalance("999");

		assertNull(balance);
	}

	@Test
	void shouldCreateAccountWhenDepositingIntoNonExistingAccount() {
		EventRequestDTO request = new EventRequestDTO("deposit", null, "100", 10);

		when(accountRepository.findById("100")).thenReturn(null);

		EventResponseDTO response = accountService.processEvent(request);

		assertNotNull(response);
		assertNotNull(response.getDestination());
		assertEquals("100", response.getDestination().getId());
		assertEquals(10, response.getDestination().getBalance());

		verify(accountRepository).saveAccount(new Account("100", 10));
	}

	@Test
	void shouldAddAmountWhenDepositingIntoExistingAccount() {
		Account existingAccount = new Account("100", 10);

		EventRequestDTO request = new EventRequestDTO("deposit", null, "100", 15);

		when(accountRepository.findById("100")).thenReturn(existingAccount);

		EventResponseDTO response = accountService.processEvent(request);

		assertNotNull(response);
		assertNotNull(response.getDestination());
		assertEquals("100", response.getDestination().getId());
		assertEquals(25, response.getDestination().getBalance());

		verify(accountRepository).saveAccount(existingAccount);
	}

	@Test
	void shouldWithdrawFromExistingAccount() {
		Account account = new Account("100", 20);

		EventRequestDTO request = new EventRequestDTO("withdraw", "100", null, 5);

		when(accountRepository.findById("100")).thenReturn(account);

		EventResponseDTO response = accountService.processEvent(request);

		assertNotNull(response);
		assertNotNull(response.getOrigin());
		assertNull(response.getDestination());

		assertEquals("100", response.getOrigin().getId());
		assertEquals(15, response.getOrigin().getBalance());

		verify(accountRepository).saveAccount(new Account("100", 15));
	}

	@Test
	void shouldReturnNullWhenWithdrawingFromNonExistingAccount() {
		EventRequestDTO request = new EventRequestDTO("withdraw", "999", null, 5);

		when(accountRepository.findById("999")).thenReturn(null);

		EventResponseDTO response = accountService.processEvent(request);

		assertNull(response);
	}

	@Test
	void shouldThrowExceptionWhenWithdrawAmountExceedsBalance() {
		Account account = new Account("100", 5);

		EventRequestDTO request = new EventRequestDTO("withdraw", "100", null, 10);

		when(accountRepository.findById("100")).thenReturn(account);

		assertThrows(InsufficientBalanceException.class, () -> accountService.processEvent(request));

		assertEquals(5, account.getBalance());

		verify(accountRepository, never()).saveAccount(any(Account.class));
	}

	@Test
	void shouldTransferBetweenExistingAccounts() {
		Account origin = new Account("100", 20);
		Account destination = new Account("300", 5);

		EventRequestDTO request = new EventRequestDTO("transfer", "100", "300", 10);

		when(accountRepository.findById("100")).thenReturn(origin);

		when(accountRepository.findById("300")).thenReturn(destination);

		EventResponseDTO response = accountService.processEvent(request);

		assertNotNull(response);

		assertEquals("100", response.getOrigin().getId());
		assertEquals(10, response.getOrigin().getBalance());

		assertEquals("300", response.getDestination().getId());
		assertEquals(15, response.getDestination().getBalance());

		verify(accountRepository).saveAccount(new Account("100", 10));

		verify(accountRepository).saveAccount(new Account("300", 15));
	}

	@Test
	void shouldReturnNullWhenTranferingFromNonExistingAccount() {
		EventRequestDTO request = new EventRequestDTO("transfer", "100", "300", 10);

		when(accountRepository.findById("100")).thenReturn(null);

		EventResponseDTO response = accountService.processEvent(request);

		assertNull(response);
	}

	@Test
	void shouldThrowExceptionWhenTransferingAmountExceedsBalance() {
		Account origin = new Account("100", 5);

		EventRequestDTO request = new EventRequestDTO("transfer", "100", "300", 10);

		when(accountRepository.findById("100")).thenReturn(origin);

		assertThrows(InsufficientBalanceException.class, () -> accountService.processEvent(request));

		assertEquals(5, origin.getBalance());

		verify(accountRepository, never()).saveAccount(any(Account.class));
	}

	@Test
	void shouldCreateANewAccountWhenDestinationNonExists() {
		Account origin = new Account("100", 50);

		EventRequestDTO request = new EventRequestDTO("transfer", "100", "300", 10);

		when(accountRepository.findById("100")).thenReturn(origin);
		when(accountRepository.findById("300")).thenReturn(null);
		
		EventResponseDTO response = accountService.processEvent(request);
		
		assertNotNull(response);
		assertNotNull(response.getDestination());
		assertEquals("300", response.getDestination().getId());
		
		verify(accountRepository).saveAccount(origin);
		verify(accountRepository).saveAccount(response.getDestination());
	}

}
