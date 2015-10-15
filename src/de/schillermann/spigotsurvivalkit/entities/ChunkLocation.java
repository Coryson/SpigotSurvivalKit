package de.schillermann.spigotsurvivalkit.entities;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkLocation {

    final private int x;
    
    final private int z;
    
    public ChunkLocation(int x, int z) {
        
        this.x = x;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}
