package de.schillermann.spigotsurvivalkit.listeners;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class JoinMessage {
    
    final private String first;
    
    final private String normally;
    
    final private String inventoryFull;
    
    final private String money;
    
    public JoinMessage(FileConfiguration config) {
        
        this.first = config.getString("join.first");
        this.normally = config.getString("join.normally");
        this.inventoryFull = config.getString("join.inventory_full");
        this.money = config.getString("join.money");
    }
    
    public String getFirst() {
        return this.first;
    }
    
    public String getNormally(String player) {
        return String.format(this.normally, player);
    }
    
    public String getInventoryFull() {
        return this.inventoryFull;
    }
    
    public String getMoney(int price) {
        return String.format(this.money, price);
    }
}
