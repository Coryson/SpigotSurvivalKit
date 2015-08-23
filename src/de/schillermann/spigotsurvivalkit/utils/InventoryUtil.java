package de.schillermann.spigotsurvivalkit.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Mario Schillermann
 */
final public class InventoryUtil {
    
    public static boolean removeItem(Player player, Material type, int amount) {
        
        ItemStack[] inventory =  player.getInventory().getContents();
        
        for(int slot = 0; slot < inventory.length; slot++) {

            if(
                inventory[slot] != null &&
                inventory[slot].getType() == type &&
                inventory[slot].getAmount() >= amount
            ) {
                int newAmount = inventory[slot].getAmount() - amount;

                if(newAmount == 0)
                    player.getInventory().clear(slot);
                else
                    inventory[slot].setAmount(newAmount);
                
                return true;
            }
        }
        return false;
    }
    
    public static boolean addItem(Player player, Material type, int amount) {
        
        int emptySlot = player.getInventory().firstEmpty();
        if(emptySlot == -1) return false;
        
        player.getInventory().setItem(emptySlot, new ItemStack(type, amount));
        return true;
    }
}
