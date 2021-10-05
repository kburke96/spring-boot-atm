package com.kevin.atm.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.kevin.atm.service.AccountService;
import com.kevin.atm.service.AtmService;

import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.HashMap;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AtmService atmService;

    @Test
    public void givenGetBalanceShouldReturnBalance() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(true);
        when(accountService.checkAvailableFunds(123456789, 1234)).thenReturn(1000.00);
        when(accountService.checkBalance(123456789, 1234)).thenReturn(800.00);
        when(atmService.maxFundsAvailable()).thenReturn(1500.00);

        this.mockMvc.perform(get("/balance?acct=123456789&pin=1234")).andDo(print()).andExpect(content().string(containsString("Balance: €800.0, Funds available: €1000.0")));
    }

    @Test
    public void givenBadCredentialsAndGetBalanceShouldReturnErrorMessage() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(false);

        this.mockMvc.perform(get("/balance?acct=123456789&pin=1234")).andDo(print()).andExpect(content().string(containsString("PIN is incorrect")));
    }

    @Test
    public void givenWithdrawShouldReturnStringOfNoteDispensed() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(true);
        when(atmService.areFundsAvailable(anyDouble())).thenReturn(true);
        when(accountService.checkAvailableFunds(123456789, 1234)).thenReturn(1000.00);
        when(accountService.checkBalance(123456789, 1234)).thenReturn(800.00);
        when(accountService.withdraw(123456789, 1234, 100)).thenReturn(true);
        when(atmService.maxFundsAvailable()).thenReturn(1500.00);

        HashMap<Double, Integer> notesHashMap = new HashMap<>();
        notesHashMap.put(50.0, 2);

        when(atmService.withdraw(anyDouble())).thenReturn(notesHashMap);

        this.mockMvc.perform(post("/withdraw?acct=123456789&pin=1234&amount=100")).andDo(print()).andExpect(content().string(containsString("You are getting: 2x €50.0 ")));
    }

    @Test
    public void givenValidCredentialsAndWithdrawInvalidAmountShouldReturnErrorMessage() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(true);
        when(atmService.areFundsAvailable(anyInt())).thenReturn(false);

        this.mockMvc.perform(post("/withdraw?acct=123456789&pin=1234&amount=100")).andDo(print()).andExpect(content().string(containsString("Sorry, insufficient funds in ATM.")));
    }

    @Test
    public void givenWithdrawInvalidAmountFromAccountShouldReturnErrorMessage() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(true);
        when(atmService.areFundsAvailable(anyDouble())).thenReturn(true);
        when(accountService.checkAvailableFunds(123456789, 1234)).thenReturn(10.00);
        when(accountService.checkBalance(123456789, 1234)).thenReturn(5.00);
        when(accountService.withdraw(123456789, 1234, 100)).thenReturn(false);
        when(atmService.maxFundsAvailable()).thenReturn(1500.00);

        String expectedString = "Insufficient funds in account.";
        this.mockMvc.perform(post("/withdraw?acct=123456789&pin=1234&amount=100")).andDo(print()).andExpect(content().string(containsString(expectedString)));
    }

    @Test
    public void givenWithdrawAndProblemWithAccountShouldReturnErrorMessage() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(true);
        when(atmService.areFundsAvailable(anyDouble())).thenReturn(true);
        when(accountService.checkAvailableFunds(123456789, 1234)).thenReturn(100.00);
        when(accountService.withdraw(123456789, 1234, 100)).thenReturn(false);

        this.mockMvc.perform(post("/withdraw?acct=123456789&pin=1234&amount=100")).andDo(print()).andExpect(content().string(containsString("Sorry, there was a problem with your withdrawal.")));
 
    }

    @Test
    public void givenBadCredentialsAndWithdrawShouldReturnErrorMessage() throws Exception {
        when(accountService.verifyCredentials(123456789, 1234)).thenReturn(false);

        this.mockMvc.perform(post("/withdraw?acct=123456789&pin=1234&amount=100")).andDo(print()).andExpect(content().string(containsString("PIN is incorrect")));
    }
}
