package de.schillermann.spigotsurvivalkit.menu.item;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class HelpersMenuItem extends MenuItem {
    
    final private List<String> description;
    
    public HelpersMenuItem(Material type, String name, List<String> description) {
     
        super(type, name, description);
        this.description = description;
    }
    
    public ItemStack getItem(List<String> helperNameList) {
        
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();
        
        if(helperNameList.isEmpty())
            meta.setLore(this.description);
        else
            meta.setLore(helperNameList);
        
        item.setItemMeta(meta);
        
        return item;
    }
}
