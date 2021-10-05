package com.kevin.atm.service;

import java.util.Optional;

import com.kevin.atm.model.Account;
import com.kevin.atm.repository.AccountRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepo; 

    public Boolean verifyCredentials(int accountNumber, int pin) {
        Boolean valid = false;

        Optional<Account> account = accountRepo.findById(accountNumber);
        if ( account.get().getPin() == pin) {
            valid = true;
        }
        return valid;
    }

    public double checkBalance(int accountNumber, int pin) {

        if (verifyCredentials(accountNumber, pin)) {
            if (accountRepo.findById(accountNumber).get().getPin() == pin) {
                return accountRepo.findById(accountNumber).get().getBalance();
            }
        } else {
            throw new RuntimeException("Balance check failed.");
        }

        return 0;
    }

    public double checkAvailableFunds(int accountNumber, int pin) {
        if (verifyCredentials(accountNumber, pin)) {
            return accountRepo.findById(accountNumber).get().getBalance() + accountRepo.findById(accountNumber).get().getOverdraft();
        } else {
            throw new RuntimeException("Available funds check failed.");
        }

    }

    public Boolean withdraw(int accountNumber, int pin, double amountToWithdraw) {
        if (verifyCredentials(accountNumber, pin)) {
            if (amountToWithdraw <= (accountRepo.findById(accountNumber).get().getBalance() + accountRepo.findById(accountNumber).get().getOverdraft())) {
                Optional<Account> optionalAccount = accountRepo.findById(accountNumber);
                Account updatedAccount = optionalAccount.get();

                //Check if we need to use overdraft or not.
                if (amountToWithdraw <= updatedAccount.getBalance()) {
                    updatedAccount.setBalance(updatedAccount.getBalance() - amountToWithdraw);
                    accountRepo.save(updatedAccount);
                    return true;
                } else if (amountToWithdraw <= (updatedAccount.getBalance() + updatedAccount.getOverdraft())) {
                    double diff = amountToWithdraw - updatedAccount.getBalance();
                    updatedAccount.setBalance(0);
                    updatedAccount.setOverdraft(updatedAccount.getOverdraft() - diff);
                    accountRepo.save(updatedAccount);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
}
