package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.entities.WarpLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpTarget {
    
    final private Location location;
    
    final private String message;
    
    public WarpTarget(WarpLocation warp) {

        this.location = warp.toLocation();
        this.message = warp.getDescription();
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public String getMessage() {
        return this.message;
    }
}
