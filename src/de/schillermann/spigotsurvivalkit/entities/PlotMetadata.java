package de.schillermann.spigotsurvivalkit.entities;

import java.util.UUID;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotMetadata {
    
    final private UUID owner;
    final private int price;
    
    public PlotMetadata(UUID owner, int price) {
        this.owner = owner;
        this.price = price;
    }
    
    public UUID getOwner() {
        return this.owner;
    }
    
    public int getPrice() {
        return this.price;
    }
}
