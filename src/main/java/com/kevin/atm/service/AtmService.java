package com.kevin.atm.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AtmService {

    private double totalFunds;
    private HashMap<Double, Integer> initialNotes = new HashMap<>();

    public AtmService() {
        totalFunds = 1500;
        initialNotes.put(50.00, 10);
        initialNotes.put(20.00, 30);
        initialNotes.put(10.00, 30);
        initialNotes.put(5.00, 20);
    }

    public Boolean areFundsAvailable(double amount) {
        return (amount <= totalFunds) ? true : false;
    }

    public double maxFundsAvailable() {
        return totalFunds;
    }

    public HashMap<Double, Integer> withdraw(double amount) {
        HashMap<Double, Integer> returnedNotes = new HashMap<>();
        returnedNotes.put(50.00, 0);
        returnedNotes.put(20.00, 0);
        returnedNotes.put(10.00, 0);
        returnedNotes.put(5.00, 0);

        if (amount > totalFunds) {
            throw new RuntimeException("Sorry, not enough cash in the ATM. Please try again.");
        } else {
            //Make a copy of the current status of the ATM
            HashMap<Double, Integer> copyInitialNotes = new HashMap<>(initialNotes);

            //Check if there is enough notes to dispense for the amount
            while (amount >= 0) {
                if (amount >= 50 && (copyInitialNotes.get(50.00) > 0)) {
                    amount-=50;
                    copyInitialNotes.put(50.00, copyInitialNotes.get(50.00)-1);
                    returnedNotes.put(50.00, returnedNotes.get(50.00)+1);
                } else if (amount >= 20 && (copyInitialNotes.get(20.00) > 0)) {
                    amount-=20;
                    copyInitialNotes.put(20.00, copyInitialNotes.get(20.00)-1);
                    returnedNotes.put(20.00, returnedNotes.get(20.00)+1);
                } else if (amount >= 10 && (copyInitialNotes.get(10.00) > 0)) {
                    amount-=10;
                    copyInitialNotes.put(10.00, copyInitialNotes.get(10.00)-1);
                    returnedNotes.put(10.00, returnedNotes.get(10.00)+1);
                } else if (amount >= 5 && (copyInitialNotes.get(5.00) > 0)) {
                    amount-=5;
                    copyInitialNotes.put(5.00, copyInitialNotes.get(5.00)-1);
                    returnedNotes.put(5.00, returnedNotes.get(5.00)+1);
                } else if (amount == 0) {
                    break;
                } else {
                    throw new RuntimeException("Invalid Amount. Not enough notes to make up your request.");
                }
            }
            //If above did not fail, make the changes to the notes HashMap
            for(Map.Entry<Double, Integer> note : returnedNotes.entrySet()) {
                if (note.getValue() > 0) {
                    initialNotes.put(note.getKey(), initialNotes.get(note.getKey()) - note.getValue());
                }
            }
        }
        return returnedNotes;
    }

    
    
    
}
