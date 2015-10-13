package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotCommandMessage {
    
    final private String releaseSuccess;
    
    final private String releaseError;
    
    public PlotCommandMessage(FileConfiguration config) {
        
        String rootPath = "command.plot.release.";
        
        this.releaseSuccess = config.getString(rootPath + "success");
        this.releaseError = config.getString(rootPath + "error");
    }
    
    public String getReleaseSuccess() {
        return this.releaseSuccess;
    }
    
    public String getReleaseError() {
        return this.releaseError;
    }
}
