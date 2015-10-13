package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class SetWarpCommandMessage {
    
    final private String setWarpSuccess;
    
    final private String setWarpError;
    
    public SetWarpCommandMessage(FileConfiguration config) {
        
        String rootPath = "command.setwarp.";
        
        this.setWarpSuccess = config.getString(rootPath + "success");
        this.setWarpError = config.getString(rootPath + "error");
    }
    
    public String getSetWarpSuccess(String warpName) {
        return String.format(this.setWarpSuccess, warpName);
    }
    
    public String getSetWarpError(String warpName) {
        return String.format(this.setWarpError, warpName);
    }
}
