package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class RemoveWarpCommandMessage {
    
    final private String removeWarpSuccess;
    
    final private String removeWarpError;
    
    public RemoveWarpCommandMessage(FileConfiguration config) {
        
        String rootPath = "command.removewarp.";
        
        this.removeWarpSuccess = config.getString(rootPath + "success");
        this.removeWarpError = config.getString(rootPath + "error");
    }
    
    public String getRemoveWarpSuccess(String warpName) {
        return String.format(this.removeWarpSuccess, warpName);
    }
    
    public String getRemoveWarpError(String warpName) {
        return String.format(this.removeWarpError, warpName);
    }
}
