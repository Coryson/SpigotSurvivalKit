package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpCommandMessage {
    
    final private String setWarpSuccess;
    
    final private String setWarpError;
    
    final private String removeWarpSuccess;
    
    final private String removeWarpError;
    
    public WarpCommandMessage(FileConfiguration config) {
        
        String rootPath = "command.warp.set.";
        
        this.setWarpSuccess = config.getString(rootPath + "success");
        this.setWarpError = config.getString(rootPath + "error");
        
        rootPath = "command.warp.remove.";
        
        this.removeWarpSuccess = config.getString(rootPath + "success");
        this.removeWarpError = config.getString(rootPath + "error");
    }
    
    public String getSetWarpSuccess(String warpName) {
        return String.format(this.setWarpSuccess, warpName);
    }
    
    public String getSetWarpError(String warpName) {
        return String.format(this.setWarpError, warpName);
    }
    
    public String getRemoveWarpSuccess(String warpName) {
        return String.format(this.removeWarpSuccess, warpName);
    }
    
    public String getRemoveWarpError(String warpName) {
        return String.format(this.removeWarpError, warpName);
    }
}
