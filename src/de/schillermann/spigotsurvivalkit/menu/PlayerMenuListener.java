package de.schillermann.spigotsurvivalkit.menu;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenuListener implements Listener {

    final private Material openMenuWithItem;
    
    final private PlayerMenu menu;
    
    public PlayerMenuListener(Material openMenuWithItem, PlayerMenu menu) {
        
        this.openMenuWithItem = openMenuWithItem;
        this.menu =  menu;
    }
    
    @EventHandler
    public void onOpenMenu(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        ItemStack itemInHand = player.getItemInHand();
        
        if(
            player.getWorld().getEnvironment() != World.Environment.NORMAL ||
            clickedBlock == null ||
            itemInHand == null
        ) return;
        
        Material itemType = itemInHand.getType();
        if(itemType != this.openMenuWithItem) return;
        
        Inventory inventory =
            this.menu.getMenu(
                player.getLocation().getChunk(),
                player.getUniqueId()
            );
        
        player.openInventory(inventory);
    }
    
    @EventHandler
    public void onSelectMenuPoint(InventoryClickEvent event) {
   
        boolean isPlayerMenu =
            event.getWhoClicked() instanceof Player &&
            event.getInventory().getHolder() instanceof PlayerMenuHolder;
    
        if(isPlayerMenu) {
            
            ItemStack item = event.getCurrentItem();
            if(item == null) return;
            
            Material select = item.getType();
            Player player = (Player) event.getWhoClicked();
            
            this.menu.selectMenuItem(player, select);
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onGetMetadata(AsyncPlayerChatEvent event) {

        event.setCancelled(
            this.menu.isMetadata(
                event.getPlayer(),
                event.getMessage()
            )
        );
    }
}
