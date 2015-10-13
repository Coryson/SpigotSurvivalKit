package de.schillermann.spigotsurvivalkit.menu.item;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotSellCancelMenuItem extends MenuItem {
    
    final private List<String> description;
    
    public PlotSellCancelMenuItem(Material type, String name, List<String> description) {
     
        super(type, name, description);
        this.description = description;
    }
    
    public ItemStack getItem(int price) {
        
        List<String> descriptionRender = this.description;
        
        if(!this.description.isEmpty()) {
            
            String descriptionWithPrice =
                String.format(this.description.get(0), price);
            
            descriptionRender.set(0, descriptionWithPrice);
        }
        
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(descriptionRender);
        item.setItemMeta(meta);
        
        return item;
    }
}
