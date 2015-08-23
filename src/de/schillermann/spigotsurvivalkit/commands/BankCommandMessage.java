package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class BankCommandMessage {
    
    final private String transfer;
    
    final private String balance;
    
    final private String playerNotFound;
    
    public BankCommandMessage(FileConfiguration config) {
        
        String rootPath = "command.bank.";
        
        this.transfer = config.getString(rootPath + "transfer");
        this.balance = config.getString(rootPath + "balance");
        
        this.playerNotFound = config.getString("player_not_found");
    }
    
    public String getTransfer(String receiver, int price) {
        return String.format(this.transfer, price, receiver);
    }
    
    public String getBalance(int price) {
        return String.format(this.balance, price);
    }
    
    public String getPlayerNotFound() {
        return this.playerNotFound;
    }
}
