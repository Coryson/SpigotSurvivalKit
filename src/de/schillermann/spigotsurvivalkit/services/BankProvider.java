package de.schillermann.spigotsurvivalkit.services;

import de.schillermann.spigotsurvivalkit.databases.tables.BankTable;
import java.util.UUID;

/**
 *
 * @author Mario Schillermann
 */
final public class BankProvider {
    
    final private BankTable bank;
    
    public BankProvider(BankTable bank) {
        this.bank = bank;
    }
    
    public boolean transfer(UUID receiver, int price) {
        
        int balance = this.bank.selectBalance(receiver);
        
        if(balance >= 0)
            return this.bank.updateBalance(receiver, balance + price);
        else
            return this.bank.insertBalance(receiver, price);
            
    }
    
    public int getBalance(UUID owner) {
        int balance = this.bank.selectBalance(owner);
        
        if(balance >= 0)
            return balance;
        else
            return 0;
    }
    
    public boolean setBalance(UUID owner, int price) {
        return this.bank.updateBalance(owner, price);
    }
}
