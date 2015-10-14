package de.schillermann.spigotsurvivalkit.menu;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpLocation;
import de.schillermann.spigotsurvivalkit.menu.type.WarpsMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpsMenu implements PlayerMenu {
    
    final private WarpTable tableWarp;
    
    final private WarpsMenuMessage message;
    
    private Inventory menu;
    
    public WarpsMenu(WarpTable tableWarp, WarpsMenuMessage message) {
        
        this.tableWarp = tableWarp;
        this.message = message;
        
        this.reloadInventory();
    }
    
    public void reloadInventory() {
        
        WarpsMenuItems items = new WarpsMenuItems(this.tableWarp);
        
        int numbersOfItems = items.getItems().length;
        int numbersOfSlot = 27;
        
        if(numbersOfItems < 10)
            numbersOfSlot = 9;
        else if (numbersOfItems < 19)
            numbersOfSlot = 18;
        
        this.menu = Bukkit.createInventory(
            new WarpsMenuHolder(),
            numbersOfSlot,
            this.message.getMenuTitle()
        );
        
        this.menu.addItem(items.getItems());
    }
    
    @Override
    public Inventory getInventory(Player player) {
        return menu;
    }
    
    @Override
    public InventoryHolder inventoryItemAction(Player player, String selectedItemName) {
        
        WarpLocation warpLocation = this.tableWarp.selectWarp(selectedItemName);
        
        if(selectedItemName.equals(this.message.getItemBedName())) {
            
            if(player.getBedSpawnLocation() == null) {
                player.sendMessage(this.message.getItemBedError());
            }
            else {
                player.teleport(player.getBedSpawnLocation());
                
                if(!warpLocation.getDescription().isEmpty())
                    player.sendMessage(
                        ChatColor.YELLOW +
                        warpLocation.getDescription()
                    );
            }
            return null;
        }
        
        Location teleportLocation = new Location(
            Bukkit.getWorld(warpLocation.getWorld()),
            warpLocation.getX(),
            warpLocation.getY(),
            warpLocation.getZ()
        );
        
        player.teleport(teleportLocation);
        
        if(!warpLocation.getDescription().isEmpty())
            player.sendMessage(
                ChatColor.YELLOW +
                warpLocation.getDescription()
            );
        
        return null;
    }
}
