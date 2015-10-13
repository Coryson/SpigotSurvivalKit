package de.schillermann.spigotsurvivalkit.menu.item;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotOwnerMenuItem extends MenuItem {
    
    final private String name;
    
    final private List<String> description;
    
    final private List<String> playerIsHelper;
    
    public PlotOwnerMenuItem(
        Material type,
        String name,
        List<String> description,
        List<String> playerIsHelper
    ) {
     
        super(type, name, description);
        this.name = name;
        this.description = description;
        this.playerIsHelper = playerIsHelper;
    }
    
    public ItemStack getItem(String owner, boolean isPlayerHelper) {
        
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(
            String.format(this.name, owner)
        );
        
        if(isPlayerHelper)
            meta.setLore(this.playerIsHelper);
        else
            meta.setLore(this.description);
        
        item.setItemMeta(meta);
        return item;
    }
}
