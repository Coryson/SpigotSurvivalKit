package de.schillermann.spigotsurvivalkit.listeners;

import org.bukkit.Location;

/**
 *
 * @author Mario Schillermann
 */
final public class Hospital {
    
    final private Location location;
    
    final private String msgInfo;
    
    public Hospital(Location location, String msgInfo) {
        
        this.location = location;
        this.msgInfo = msgInfo;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public String getinfo() {
        return this.msgInfo;
    }
}
