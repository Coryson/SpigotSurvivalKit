package de.schillermann.spigotsurvivalkit.listeners;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class LockMessage {
    
    final private String chestOwner;
    
    final private String chestLocked;
    
    final private String chestUnlocked;
    
    final private String doorOwner;
    
    final private String doorLocked;
    
    final private String doorUnlocked;
    
    public LockMessage(FileConfiguration config) {
        
        this.chestOwner = config.getString("lock.chest.owner");
        this.chestLocked = config.getString("lock.chest.locked");
        this.chestUnlocked = config.getString("lock.chest.unlocked");
        this.doorOwner = config.getString("lock.door.owner");
        this.doorLocked = config.getString("lock.door.locked");
        this.doorUnlocked = config.getString("lock.door.unlocked");
    }
    
    public String getChestOwner(String name) {
        return String.format(this.chestOwner, name);
    }
    
    public String getChestLocked() {
        return this.chestLocked;
    }
    
    public String getChestUnlocked() {
        return this.chestUnlocked;
    }
    
    public String getDoorOwner(String name) {
        return String.format(this.doorOwner, name);
    }
    
    public String getDoorLocked() {
        return this.doorLocked;
    }
    
    public String getDoorUnlocked() {
        return this.doorUnlocked;
    }
}
