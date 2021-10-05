package com.kevin.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kevin.atm.model.Account;
import com.kevin.atm.repository.AccountRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepo;

    private AccountService accountService;

    Account account1 = new Account(1234,1234,100.00,100.00);
    Account account2 = new Account(4321,9876,1500.00,200.00);

    List<Account> accountList;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(accountRepo);

        accountList = new ArrayList<Account>();
        accountList.add(account1);
        accountList.add(account2);
    }

    @AfterEach
    public void tearDown() {
        accountService = null;
        accountList = null;
        account1 = account2 = null;
    }

    @Test
    public void givenValidCredentialsShouldReturnTrue() {
        when(accountRepo.findById(1234)).thenReturn(Optional.of(account1));
        Boolean isValidCredentials = accountService.verifyCredentials(1234, 1234);
        assertTrue(isValidCredentials);
    }

    @Test
    public void givenInvalidCredentialsShouldReturnFalse() {
        when(accountRepo.findById(1234)).thenReturn(Optional.of(account1));
        Boolean isValidCredentials = accountService.verifyCredentials(1234, 0000);
        assertFalse(isValidCredentials);
    }

    @Test
    public void givenValidCredentialsAndCheckBalanceShouldReturnBalance() {
        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));
        double returnedBalance = accountService.checkBalance(1234, 1234);
        assertEquals(100.00, returnedBalance);
    }

    @Test
    public void givenInvalidCredentialsAndCheckBalanceShouldThrowRuntimeException() {
        when(accountRepo.findById(1234)).thenReturn(Optional.of(account2));

        Exception thrown = assertThrows(
            RuntimeException.class, 
            () -> accountService.checkBalance(1234, 1234));

        assertEquals("Balance check failed.", thrown.getMessage());
    }

    @Test
    public void givenValidCredentialsAndCheckAvailableFunds_shouldReturnAvailableFunds() {
        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));
        double returnedBalance = accountService.checkAvailableFunds(1234, 1234);
        assertEquals(200.00, returnedBalance);
    }

    @Test
    public void givenInvalidCredentialsAndCheckAvailableFundsShouldThrowRuntimeException() {
        when(accountRepo.findById(1234)).thenReturn(Optional.of(account2));

        Exception thrown = assertThrows(
            RuntimeException.class, 
            () -> accountService.checkAvailableFunds(1234, 1234));

        assertEquals("Available funds check failed.", thrown.getMessage());
    }

    @Test
    public void givenValidCredentialsAndWithdrawValidAmountShouldReturnTrue() {
        // Account updatedAccount1 = new Account(1234,1234,90.00,100.00);

        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));
        when(accountRepo.save(any(Account.class))).then(returnsFirstArg());

        Boolean answer = accountService.withdraw(1234,1234,10.00);

        assertTrue(answer);
    }

    @Test
    public void givenValidCredentialsAndWithdrawValidAmountUsingOverdraftShouldReturnTrue() {
        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));
        when(accountRepo.save(any(Account.class))).then(returnsFirstArg());

        Boolean answer = accountService.withdraw(1234,1234,150.00);

        assertTrue(answer);
    }

    @Test
    public void givenValidCredentialsAndWithdrawInvalidAmountUsingOverdraftShouldReturnFalse() {
        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));

        Boolean answer = accountService.withdraw(1234,1234,500.00);

        assertFalse(answer);
    }

    @Test
    public void givenInvalidCredentialsAndWithdrawShouldReturnFalse() {
        when(accountRepo.findById(anyInt())).thenReturn(Optional.of(account1));

        Boolean answer = accountService.withdraw(1234, 0000, 100.00);

        assertFalse(answer);
    }
    

    
}
