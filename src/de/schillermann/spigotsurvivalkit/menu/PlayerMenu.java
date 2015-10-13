package de.schillermann.spigotsurvivalkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 *
 * @author Mario Schillermann
 */
public interface PlayerMenu {
    
    Inventory getInventory(Player player);
    
    /**
     * After clicking on an item an action is executed
     * @param player Player who clicked on the item
     * @param selectedItemName Selected item name from inventory
     * @return If a sub-menu you want to open, otherwise null
     */
    InventoryHolder inventoryItemAction(Player player, String selectedItemName);
}
