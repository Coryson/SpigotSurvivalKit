package de.schillermann.spigotsurvivalkit.menu.warps;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.entities.WarpLocation;
import de.schillermann.spigotsurvivalkit.menu.PlayerMenu;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        
        WarpLocation warpLocation =
            this.getWarpLocation(player, selectedItemName);
        
        if(warpLocation == null) {
            player.sendMessage(this.message.getItemBedError());
        }
        else {
            
            int volume = 10;
            int pitch = 1;
            
            Location teleportFrom = player.getLocation();
            Location teleportTo = warpLocation.toLocation();
            World worldFrom = teleportFrom.getWorld();
            World worldTo = teleportTo.getWorld();
            
            worldFrom.playSound(
                teleportFrom,
                Sound.ENDERMAN_STARE,
                volume,
                pitch
            );
            
            worldFrom.playEffect(
                teleportFrom,
                Effect.ENDER_SIGNAL,
                0
            );
            
            if(player.teleport(teleportTo)) {
                
                worldTo.playSound(
                    teleportTo,
                    Sound.ENDERMAN_TELEPORT,
                    volume,
                    pitch
                );
                
                worldTo.playEffect(
                    teleportTo,
                        Effect.ENDER_SIGNAL,
                        1
                );
                
                if(!warpLocation.getDescription().isEmpty())
                    player.sendMessage(
                        ChatColor.YELLOW +
                        warpLocation.getDescription()
                    );
            }
            
            
        }
        
        return null;
    }
    
    private WarpLocation getWarpLocation(Player player, String selectedItemName) {
        
        WarpLocation warpLocation = this.tableWarp.selectWarp(selectedItemName);
        
        if(selectedItemName.equals(this.message.getItemBedName())) {
            
            Location location = player.getBedSpawnLocation();
            if(location == null) return null;
            
            return new WarpLocation(
                location.getWorld().getUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                warpLocation.getDescription()
            );
        }
        
        return warpLocation;
    }
}
