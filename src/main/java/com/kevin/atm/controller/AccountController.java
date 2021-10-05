package com.kevin.atm.controller;

import java.util.HashMap;
import java.util.Map;

import com.kevin.atm.service.AccountService;
import com.kevin.atm.service.AtmService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController("/atm")
@AllArgsConstructor
public class AccountController {
    
    AccountService accountService;

    AtmService atmService;

    @GetMapping(value = "/balance")
    public String getBalance(@RequestParam("acct") int accountNumber,
                                             @RequestParam("pin") int pin) {
        if (accountService.verifyCredentials(accountNumber, pin)) {
            double maxAvailable = (accountService.checkAvailableFunds(accountNumber, pin) > atmService.maxFundsAvailable()) ? atmService.maxFundsAvailable() : accountService.checkAvailableFunds(accountNumber, pin);                                         
            return "Balance: €" + accountService.checkBalance(accountNumber, pin) + ", Funds available: €" + maxAvailable;
        } else {
            return "PIN is incorrect";
        }                                      
    }

    @PostMapping(value = "/withdraw")
    public String withdraw(@RequestParam("acct") int accountNumber,
                                            @RequestParam("pin") int pin,
                                            @RequestParam("amount") double amountToWithdraw) {
        if (accountService.verifyCredentials(accountNumber, pin)) {
            if (!atmService.areFundsAvailable(amountToWithdraw)) {
                return "Sorry, insufficient funds in ATM.";
            } else if (amountToWithdraw > accountService.checkAvailableFunds(accountNumber, pin)) {
                return "Insufficient funds in account.";
            } else {
                if (accountService.withdraw(accountNumber, pin, amountToWithdraw)) {
                    HashMap<Double, Integer> dispensedNotes = atmService.withdraw(amountToWithdraw);
                    StringBuilder returnString = new StringBuilder();
                    returnString.append("You are getting: ");
                    for (Map.Entry<Double, Integer> note : dispensedNotes.entrySet()) {
                        if (note.getValue() > 0) {
                            returnString.append(note.getValue() + "x €" + note.getKey() + " ");
                        }
                    }
                    returnString.append("\nRemaining balance: €" + accountService.checkBalance(accountNumber, pin));
                    return returnString.toString();
                } else {
                    return "Sorry, there was a problem with your withdrawal.";
                }
            }
        } else {
            return "PIN is incorrect";
        }  
        
        
    }
}
