package de.schillermann.spigotsurvivalkit.menu;

import de.schillermann.spigotsurvivalkit.menu.warps.WarpsMenu;
import de.schillermann.spigotsurvivalkit.menu.main.MainMenu;
import de.schillermann.spigotsurvivalkit.menu.main.MainMenuHolder;
import de.schillermann.spigotsurvivalkit.menu.warps.WarpsMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenuListener implements Listener {

    final private Plugin plugin;
    
    final private Material openMenuWithItem;
    
    final private MainMenu menuMain;
    
    final private WarpsMenu menuWarps;
    
    public PlayerMenuListener(
        Plugin plugin,
        Material openMenuWithItem,
        MainMenu menuMain,
        WarpsMenu menuWarps
    ) {
        
        this.plugin = plugin;
        this.openMenuWithItem = openMenuWithItem;
        this.menuMain =  menuMain;
        this.menuWarps =  menuWarps;
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
        
        int volume = 10;
        int pitch = 1;

        player.playSound(
            player.getLocation(),
            Sound.CHICKEN_EGG_POP,
            volume,
            pitch
        );
        
        Inventory inventory = this.menuMain.getInventory(player);
        
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            player.openInventory(inventory);
        });
    }
    
    @EventHandler
    public void onSelectMenuPoint(InventoryClickEvent event) {
   
        if(!(event.getWhoClicked() instanceof Player)) return;
        
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        
        InventoryHolder menuType = event.getInventory().getHolder();
        String selectedItemTitle = meta.getDisplayName();
        Player player = (Player) event.getWhoClicked();
        
        if(!(menuType instanceof CustomInventory)) return;
        
        event.setCancelled(true);
        
        if(menuType instanceof MainMenuHolder) {
            
            InventoryHolder subMenu =
                this.menuMain.inventoryItemAction(player, selectedItemTitle);
            
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                player.closeInventory();
            });
            
            if(subMenu instanceof WarpsMenuHolder) {
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    player.openInventory(this.menuWarps.getInventory(player));
                });
            }
        }
        else if(menuType instanceof WarpsMenuHolder) {
            
            this.menuWarps.inventoryItemAction(player, selectedItemTitle);
        }
    }
    
    @EventHandler
    public void onGetMetadata(AsyncPlayerChatEvent event) {

        event.setCancelled(
            this.menuMain.isMetadata(
                event.getPlayer(),
                event.getMessage()
            )
        );
    }
}
