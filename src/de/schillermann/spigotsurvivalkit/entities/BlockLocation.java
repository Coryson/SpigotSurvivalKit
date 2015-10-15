package de.schillermann.spigotsurvivalkit.entities;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author Mario Schillermann
 */
final public class BlockLocation {

    final private UUID world;
    
    final private int x;
    
    final private int y;
    
    final private int z;
    
    public BlockLocation(UUID world, int x, int y, int z) {
        
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public BlockLocation(Location block) {
        
        this.world = block.getWorld().getUID();
        this.x = block.getBlockX();
        this.y = block.getBlockY();
        this.z = block.getBlockZ();
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
}
