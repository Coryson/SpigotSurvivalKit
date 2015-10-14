package de.schillermann.spigotsurvivalkit.databases.tables.entities;

import java.util.UUID;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpLocation {

    final private UUID world;
    
    final private double x;
    
    final private double y;
    
    final private double z;
    
    final private String description;
    
    public WarpLocation(UUID world, double x, double y, double z, String description) {
        
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.description = description;
    }
    
    public UUID getWorld() {
        return this.world;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
    
    public String getDescription() {
        return this.description;
    }
}
