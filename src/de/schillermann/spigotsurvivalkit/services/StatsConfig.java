package de.schillermann.spigotsurvivalkit.services;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class StatsConfig {
    
    final private int number;
    
    final private String headline;
            
    final private String row;
    
    public StatsConfig(FileConfiguration config) {
        
        this.number = config.getInt("stats.v");
        this.headline = config.getString("stats.headline");
        this.row = config.getString("stats.row");
    }
    
    
    public int getNumber() {
        return this.number;
    }
    
    public String getHeadline() {
        return this.headline;
    }
    
    public String getRow(String player, int diamond) {
        return String.format(this.row, player, diamond);
    }
}
