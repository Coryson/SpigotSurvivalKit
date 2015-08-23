package de.schillermann.spigotsurvivalkit.databases.tables.entities;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkLocation {

    final int x;
    final int z;
    
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
