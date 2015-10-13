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
    
    final private String title;
    
    final private WarpTable tableWarp;
    
    private Inventory menu;
    
    public WarpsMenu(String title, WarpTable tableWarp) {
        
        this.title = title;
        this.tableWarp = tableWarp;
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
            this.title
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
        
        Location teleportLocation = new Location(
            Bukkit.getWorld(warpLocation.getWorld()),
            warpLocation.getX(),
            warpLocation.getY(),
            warpLocation.getZ()
        );
        
        player.teleport(teleportLocation);
        player.sendMessage(ChatColor.YELLOW + warpLocation.getDescription());
        
        return null;
    }
}
