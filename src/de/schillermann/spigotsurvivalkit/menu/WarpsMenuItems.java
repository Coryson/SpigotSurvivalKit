package de.schillermann.spigotsurvivalkit.menu;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpItem;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpsMenuItems {
    
    final private ItemStack[] warpItems;
    
    public WarpsMenuItems(WarpTable tableWarp) {
        
        List<ItemStack> warpItemList = new ArrayList<>();
        
        tableWarp.selectWarpItems().stream().map((warp) -> {
            ItemStack item = new ItemStack(warp.getWarpItem());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(warp.getWarpName());
            meta.setLore(convertItemDescription(warp.getWarpDescription()));
            item.setItemMeta(meta);
            return item;
        }).forEach((item) -> {
            warpItemList.add(item);
        });
        
        this.warpItems = warpItemList.toArray(new ItemStack[warpItemList.size()]);
    }
    
    public ItemStack[] getItems() {
        return this.warpItems;
    }
    
    private static List<String> convertItemDescription(String description) {
        
        List<String> descriptionList = new ArrayList<>();
        String[] descriptionElements = description.split(" ");
        StringJoiner descriptionJoiner = null;
        int charCounter = 0;
        
        for (String descriptionElement : descriptionElements) {
            
            if(charCounter == 0)
                descriptionJoiner = new StringJoiner(" ", "", "");
            
            charCounter += descriptionElement.length();
            descriptionJoiner.add(descriptionElement);
            
            if(charCounter > 10) {
                descriptionList.add(descriptionJoiner.toString());
                charCounter = 0;
            }
        }
        
        if(charCounter > 0)
            descriptionList.add(descriptionJoiner.toString());
            
        return descriptionList;
    }
}
