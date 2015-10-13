package de.schillermann.spigotsurvivalkit.databases.tables.entities;

import java.util.UUID;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpLocation {

    final private UUID world;
    
    final private int x;
    
    final private int y;
    
    final private int z;
    
    final private String description;
    
    public WarpLocation(UUID world, int x, int y, int z, String description) {
        
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.description = description;
    }
    
    public UUID getWorld() {
        return this.world;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
    
    public String getDescription() {
        return this.description;
    }
}
