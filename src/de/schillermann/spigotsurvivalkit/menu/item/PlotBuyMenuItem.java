package de.schillermann.spigotsurvivalkit.menu.item;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotBuyMenuItem extends MenuItem {
    
    final private List<String> description;
    
    public PlotBuyMenuItem(Material type, String name, List<String> description) {
     
        super(type, name, description);
        this.description = description;
    }
    
    public ItemStack getItem(String seller, int price) {
        
        int indexOwner = 0;
        int indexPrice = 1;
        List<String> descriptionRender = this.description;
        
        if(this.description.size() > indexOwner) {
            
            String descriptionWithOwner =
                String.format(this.description.get(indexOwner), seller);
            
            descriptionRender.set(indexOwner, descriptionWithOwner);
        }
        
        if(this.description.size() > indexPrice) {
            
            String descriptionWithPrice =
                String.format(this.description.get(indexPrice), price);
            
            descriptionRender.set(indexPrice, descriptionWithPrice);
        }
        
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(descriptionRender);
        item.setItemMeta(meta);
        
        return item;
    }
}
