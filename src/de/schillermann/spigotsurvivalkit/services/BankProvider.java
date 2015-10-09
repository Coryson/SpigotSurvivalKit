package de.schillermann.spigotsurvivalkit.services;

import de.schillermann.spigotsurvivalkit.databases.tables.BankTable;
import java.util.UUID;
import org.bukkit.Material;

/**
 *
 * @author Mario Schillermann
 */
final public class BankProvider {
    
    final private BankTable bank;
    
    final private Material currency;
    
    public BankProvider(BankTable bank, Material currency) {
        this.bank = bank;
        this.currency = currency;
    }
    
    public boolean transfer(UUID receiver, int price) {
        
        int balance = this.bank.selectBalance(receiver);
        
        if(balance >= 0)
            return this.bank.updateBalance(receiver, balance + price);
        else
            return this.bank.insertBalance(receiver, price);
            
    }
    
    public boolean setBalance(UUID owner, int price) {
        return this.bank.updateBalance(owner, price);
    }
    
    public int getBalance(UUID owner) {
        int balance = this.bank.selectBalance(owner);
        
        if(balance >= 0)
            return balance;
        else
            return 0;
    }
    
    public Material GetCurrency() {
        return this.currency;
    }
}
