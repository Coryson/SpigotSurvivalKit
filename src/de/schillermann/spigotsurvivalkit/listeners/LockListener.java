package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.databases.tables.LockTable;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.BlockLocation;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

/**
 *
 * @author Mario Schillermann
 */
final public class LockListener implements Listener {
    
    final private LockTable lock;
    
    final private LockMessage message;
    
    public LockListener(LockTable lock, LockMessage message) {
        this.lock = lock;
        this.message = message;
    }
    
    @EventHandler
    public void onCanOpen(PlayerInteractEvent event) {

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Block block = event.getClickedBlock();
        Material type = block.getType();
        
        if(!canLock(type)) return;
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        
        UUID ownerUuid =
            this.lock.selectPlayerUuidFromLock(
                new BlockLocation(block.getLocation())
            );
        
        if(ownerUuid == null) return;
        
        if(playerUuid.toString().equals(ownerUuid.toString())) {
        
            if(type == Material.IRON_DOOR_BLOCK) {
                BlockState state = block.getState();
                Openable doorState = (Openable) state.getData();

                doorState.setOpen(!doorState.isOpen());
                state.setData((MaterialData) doorState);
                state.update();
            }
        }
        else {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUuid);
   
            if(type == Material.CHEST) {
                event.setCancelled(true);
                player.sendMessage(this.message.getChestOwner(owner.getName()));
            }
            else {
                player.sendMessage(this.message.getDoorOwner(owner.getName()));
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        
        Block block = event.getBlockPlaced();
        Material blockType = block.getType();
        
        if(!canLock(blockType)) return;
        
        Player player = event.getPlayer();
        
        boolean isInsertLock =
            this.lock.insertLock(
                new BlockLocation(block.getLocation()),
                blockType,
                player.getUniqueId()
            );
        
        if(isInsertLock) {
            
            if(blockType == Material.CHEST)
                player.sendMessage(this.message.getChestLocked());
            else
                player.sendMessage(this.message.getDoorLocked());
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        
        Block block = event.getBlock();
        Material type = block.getType();
        if(!canLock(type)) return;
        
        Player player = event.getPlayer();
        
        if(this.lock.deleteLock(new BlockLocation(block.getLocation()))) {
            if(type != Material.CHEST)
                player.sendMessage(this.message.getChestUnlocked());
            else
                player.sendMessage(this.message.getDoorUnlocked());
        }
    }
    
    private boolean canLock(Material type) {
        return type == Material.CHEST || type == Material.IRON_DOOR_BLOCK;
    }
}
