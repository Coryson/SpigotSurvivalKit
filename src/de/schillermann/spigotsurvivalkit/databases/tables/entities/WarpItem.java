package de.schillermann.spigotsurvivalkit.databases.tables.entities;

import org.bukkit.Material;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpItem {
    
    final private String warpName;
    
    final private String warpDescription;
    
    final private Material warpItem;
    
    public WarpItem(String warpName, String warpDescription, Material warpItem) {
        
        this.warpName = warpName;
        this.warpDescription = warpDescription;
        this.warpItem = warpItem;
    }
    
    public String getWarpName() {
        return this.warpName;
    }
    
    public String getWarpDescription() {
        return this.warpDescription;
    }
    
    public Material getWarpItem() {
        return this.warpItem;
    }
}
