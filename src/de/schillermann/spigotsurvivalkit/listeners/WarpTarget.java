package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpTarget {
    
    final private Location location;
    
    final private String message;
    
    public WarpTarget(WarpLocation location) {
        
        this.location = new Location(
            Bukkit.getWorld(location.getWorld()),
            location.getX(),
            location.getY(),
            location.getZ()
        );
        
        this.message = location.getDescription();
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public String getMessage() {
        return this.message;
    }
}
