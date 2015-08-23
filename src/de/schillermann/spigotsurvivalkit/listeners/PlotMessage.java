package de.schillermann.spigotsurvivalkit.listeners;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotMessage {
    
    final private String forbiddenBreak;
        
    final private String forbiddenBuild;
    
    final private String forbiddenPvpAttacker;
            
    final private String forbiddenPvpAffected;
    
    final private String regionWilderness;
    
    final private String regionPlot;
    
    public PlotMessage(FileConfiguration config) {
        
        this.forbiddenBreak = config.getString("plot.forbidden.break");
        this.forbiddenBuild = config.getString("plot.forbidden.build");
        this.forbiddenPvpAttacker = config.getString("plot.forbidden.pvp.attacker");
        this.forbiddenPvpAffected = config.getString("plot.forbidden.pvp.affected");
        this.regionWilderness = config.getString("plot.region.wilderness");
        this.regionPlot = config.getString("plot.region.plot");
        
    }
    
    public String getForbiddenBreak() {
        return this.forbiddenBreak;
    }
        
    public String getForbiddenBuild() {
        return this.forbiddenBuild;
    }
    
    public String getForbiddenPvpAttacker() {
        return this.forbiddenPvpAttacker;
    }
    
    public String getForbiddenPvpAffected() {
        return this.forbiddenPvpAffected;
    }
    
    public String getRegionWilderness() {
        return this.regionWilderness;
    }
    
    public String getRegionPlot() {
        return this.regionPlot;
    }
}
