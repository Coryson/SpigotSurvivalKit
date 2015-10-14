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
    
    final private String empty;
    
    final private String footer;
    
    public StatsConfig(FileConfiguration config) {
        
        this.number = config.getInt("stats.number");
        this.row = config.getString("stats.row");
        this.empty = config.getString("stats.empty");
        this.footer = config.getString("stats.footer");
        this.headline = String.format(
            config.getString("stats.headline"),
            this.number
        );
    }
    
    
    public int getNumber() {
        return this.number;
    }
    
    public String getHeadline() {
        return this.headline;
    }
    
    public String getRow(String player, int money) {
        return String.format(this.row, player, money);
    }
    
    public String getEmpty() {
        return this.empty;
    }
    
    public String getFooter() {
        return this.footer;
    }
}
