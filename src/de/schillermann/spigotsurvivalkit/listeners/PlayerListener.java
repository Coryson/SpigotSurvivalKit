package de.schillermann.spigotsurvivalkit.listeners;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.metadata.MetadataValue;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerListener implements Listener {
    /*
    final ChunkProtectTable chunkProtectTable;
    final ChunkProtectCache chunkProtectCache;
    final String msgOwnerFree;
    final String msgOwnerYou;
    final String msgOwnerOther;
    final String msgChest;
    final String msgForbiddenUse;
    final String msgDoorPrivate;
    
    public PlayerListener(
        ChunkProtectTable chunkProtectTable,
        ChunkProtectCache chunkProtectCache,
        String msgOwnerFree,
        String msgOwnerYou,
        String msgOwnerOther,
        String msgChest,
        String msgForbiddenUse,
        String msgDoorPrivate
    ) {
        this.chunkProtectTable = chunkProtectTable;
        this.chunkProtectCache = chunkProtectCache;
        this.msgOwnerFree = msgOwnerFree;
        this.msgOwnerYou = msgOwnerYou;
        this.msgOwnerOther = msgOwnerOther;
        this.msgChest = msgChest;
        this.msgForbiddenUse = msgForbiddenUse;
        this.msgDoorPrivate = msgDoorPrivate;
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerUseItemInHand(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if(
            player == null ||
            player.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        ItemStack itemInHand = player.getItemInHand();
        Block clickedblock = event.getClickedBlock();
        
        if(itemInHand == null || clickedblock == null) return;
        
        Chunk chunkFromClickedBlock = clickedblock.getLocation().getChunk();
        Material itemType = itemInHand.getType();
        UUID playerFromChunk;

        if(itemType == Material.STICK) {
            
            playerFromChunk =
                this.chunkProtectCache.getPlayerUuid(chunkFromClickedBlock);
            
            if(playerFromChunk == null) {
                player.sendMessage(this.msgOwnerFree);
                return;
            }
            
            if(playerFromChunk.equals(player.getUniqueId())) {
                player.sendMessage(this.msgOwnerYou);
                return;
            }
            OfflinePlayer offlinePlayer =
                Bukkit.getOfflinePlayer(playerFromChunk);

            player.sendMessage(
                String.format(this.msgOwnerOther, offlinePlayer.getName())
            );
            
            return;
        }
        
        if(event.isCancelled()) return;
        
        if(
            itemType == Material.WATER_BUCKET ||
            itemType == Material.LAVA_BUCKET ||
            itemType == Material.FLINT_AND_STEEL
        )
        {
            playerFromChunk =
                this.chunkProtectCache.getPlayerUuid(chunkFromClickedBlock);
                        
            if(
                player.getUniqueId().equals(playerFromChunk) ||
                !this.chunkProtectTable.isAdjacentChunkProtect(chunkFromClickedBlock)
            ) return;
            
            player.sendMessage(this.msgForbiddenUse);
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUseBlock(PlayerInteractEvent event) {

        Block block = event.getClickedBlock();
        
        if(block == null) return;
        Player player = event.getPlayer();
        
        if(
            player == null ||
            player.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        BlockState blockState = block.getState();
        UUID playerFromChunk;
        
        Chunk chunkFromClickedBlock = block.getLocation().getChunk();
        
        if(block.getType() == Material.IRON_DOOR_BLOCK) {

            playerFromChunk =
                this.chunkProtectCache.getPlayerUuid(
                    chunkFromClickedBlock
                );
            
            if(playerFromChunk == null) return;
            
            if(!player.getUniqueId().equals(playerFromChunk)) {
                
                OfflinePlayer offlinePlayer =
                    Bukkit.getOfflinePlayer(playerFromChunk);
                
                player.sendMessage(
                    String.format(this.msgDoorPrivate, offlinePlayer.getName())
                );
                return;
            }
            
            Openable doorState = (Openable) blockState.getData();
            
            doorState.setOpen(!doorState.isOpen());
            blockState.setData((MaterialData) doorState);
            blockState.update();

            return;
        }
        
        if(event.isCancelled()) return;
        
        if(block.getType() == Material.CHEST) {

            /**
             * Wenn alle Spieler ihre Kisten neu gesetzt haben,
             * kann bis zu for Schleife alles weg
             *
            List<MetadataValue> metadata = block.getMetadata("playerUuid");
            
            for(MetadataValue value : metadata) {
                if(!player.getUniqueId().toString().equals(value.asString())) {
                    event.setCancelled(true);
                    player.sendMessage("\u00A74Finger weg von Sachen anderer!");
                }
                return;
            }
            
            playerFromChunk =
                this.chunkProtectCache.getPlayerUuid(
                    chunkFromClickedBlock
                );
            
            if(
                playerFromChunk != null &&
                !player.getUniqueId().equals(playerFromChunk)
            ) {
                event.setCancelled(true);
                player.sendMessage(this.msgChest);
            }
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerRotateItemInFrame(PlayerInteractEntityEvent event) {

        if(event.isCancelled()) return;
        
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        
        if(entity == null || player == null) return;

        Chunk chunk = player.getLocation().getChunk();
        
        if(entity.getType() != EntityType.ITEM_FRAME) return;
        
        UUID playerUuidFromProtectChunk = this.chunkProtectCache.getPlayerUuid(chunk);  
        if(playerUuidFromProtectChunk == null) return;

        if(!playerUuidFromProtectChunk.equals(player.getUniqueId()))
            event.setCancelled(true);
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        if(event.isCancelled()) return;
        
        Entity damager = event.getDamager();
        
        if(
            damager == null ||
            damager.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        // Wenn Schaden bereits von einem anderen Plugin verboten wurden 
        if(event.isCancelled() || event.getEntityType() != EntityType.PLAYER) return;

        // PvP aus
        if(
            damager.getType() == EntityType.PLAYER ||
            event.getCause() == DamageCause.PROJECTILE ||
            damager.getType() == EntityType.SPLASH_POTION
        ) {
                
            Chunk chunkFromPlayer = event.getEntity().getLocation().getChunk();

            if(this.chunkProtectCache.getPlayerUuid(chunkFromPlayer) != null)
                event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST) 
    public void onUnhangingItemFrameAndPainting(HangingBreakByEntityEvent event) {

        if(event.isCancelled()) return;
        
        Entity remover = event.getRemover();
        
        if(
            remover == null ||
            remover.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
                
        UUID playerFromChunk =
            this.chunkProtectCache.getPlayerUuid(
                event.getEntity().getLocation().getChunk()
            );
        
        if(
            playerFromChunk == null ||
            playerFromChunk.equals(remover.getUniqueId())
        ) return;
        
        event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGH) 
    public void onRemovingItemOutItemFrame(EntityDamageByEntityEvent event) {

        if(event.isCancelled()) return;
        
        Entity entity = event.getEntity();
        
        if(
            entity == null ||
            entity.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        Entity damager = event.getDamager();
        
        if(
            damager == null ||
            event.getEntityType() != EntityType.ITEM_FRAME
        ) return;
        
        UUID playerFromChunk =
            this.chunkProtectCache.getPlayerUuid(
                entity.getLocation().getChunk()
            );
        
        if(
            playerFromChunk == null ||
            playerFromChunk.equals(damager.getUniqueId())
        ) return;
        
        event.setCancelled(true);
    }*/
}
