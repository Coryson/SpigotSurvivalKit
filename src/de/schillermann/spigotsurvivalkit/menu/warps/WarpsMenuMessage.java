package de.schillermann.spigotsurvivalkit.menu.warps;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpsMenuMessage {
    
    final private String menuTitle;
    
    final private String itemBedName;
    
    final private String itemBedError;
    
    public WarpsMenuMessage(FileConfiguration config) {
        
        String rootPath = "warpsmenu.";
        this.menuTitle = config.getString(rootPath + "menu_title");
        
        String bedPath = rootPath + "items.bed.";
        this.itemBedName = config.getString(bedPath + "name");
        this.itemBedError = config.getString(bedPath + "error");
    }
    
    public String getMenuTitle() {
        return this.menuTitle;
    }
    
    public String getItemBedName() {
        return this.itemBedName;
    }
    
    public String getItemBedError() {
        return this.itemBedError;
    }
}
