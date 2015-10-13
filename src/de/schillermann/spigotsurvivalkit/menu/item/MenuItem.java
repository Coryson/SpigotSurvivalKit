package de.schillermann.spigotsurvivalkit.menu.item;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
public class MenuItem {
    
    final private String name;
    
    final private ItemStack item;
    
    public MenuItem(Material type, String name, List<String> description) {

        this.name = name;
        this.item = new ItemStack(type);
        
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(this.name);
        meta.setLore(description);
        this.item.setItemMeta(meta);
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public String getName() {
        return this.name;
    }
}
