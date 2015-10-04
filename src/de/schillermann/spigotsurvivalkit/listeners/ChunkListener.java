package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.cache.ChunkLogCache;
import de.schillermann.spigotsurvivalkit.cache.PlotCache;
import de.schillermann.spigotsurvivalkit.cache.HelperCache;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkListener implements Listener {
    
    final private PlotCache cachePlot;
    
    final private ChunkLogCache cacheChunkLog;
    
    final private HelperCache cacheHelper;
    
    final private PlotMessage message;
    
    final private List<Integer> playerInWildernessList;
    
    public ChunkListener(
        PlotCache cachePlot,
        ChunkLogCache cacheChunkLog,
        HelperCache cacheHelper,
        PlotMessage message
    ) {
        this.cachePlot = cachePlot;
        this.cacheChunkLog = cacheChunkLog;
        this.cacheHelper = cacheHelper;
        this.message = message;
        this.playerInWildernessList = new ArrayList<>();
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        
        Integer hash = event.getPlayer().hashCode();
        
        if(this.playerInWildernessList.contains(hash)) 
            this.playerInWildernessList.remove(hash);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        event.setCancelled(this.isBuildNotAllow(player, chunk));
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();

        event.setCancelled(this.isBuildNotAllow(player, chunk));
    }
    
    
    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        
        Entity attacker = event.getDamager();
        
        if(
            attacker == null ||
            attacker.getWorld().getEnvironment() != World.Environment.NORMAL ||
            event.getEntityType() != EntityType.PLAYER
        ) return;
        
        boolean isAttackerPlayer = attacker.getType() == EntityType.PLAYER;
        // PvP off
        if(
            isAttackerPlayer ||
            event.getCause() == DamageCause.PROJECTILE ||
            attacker.getType() == EntityType.SPLASH_POTION
        ) {
            Player affected = (Player)event.getEntity();
            
            Chunk chunkFromAffected = affected.getLocation().getChunk();
            if(this.cachePlot.getOwnerUuidFromPlot(chunkFromAffected) == null) return;
            
            if(isAttackerPlayer)
                attacker.sendMessage(this.message.getForbiddenPvpAttacker());
            
            affected.sendMessage(this.message.getForbiddenPvpAffected());
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event){
        
        if(event.getLocation().getWorld().getEnvironment() != World.Environment.NORMAL) return;
        
        Chunk chunk = event.getLocation().getChunk();
        boolean isWilderness =
            this.cachePlot.getOwnerUuidFromPlot(chunk) == null;
        
        if(isWilderness)
            this.cacheChunkLog.addChunk(chunk);
        else
            event.setCancelled(true);
    }
    
    private boolean isBuildNotAllow(Player player, Chunk chunk) {
        
        if(player.getWorld().getEnvironment() != World.Environment.NORMAL)
            return false;
        
        UUID playerUuid = player.getUniqueId();
        Integer playerUuidHash = playerUuid.hashCode();
        
        UUID ownerUuidFromPlot = this.cachePlot.getOwnerUuidFromPlot(chunk);

        if(ownerUuidFromPlot == null) { // Player in Wilderness
            
            this.cacheChunkLog.addChunk(chunk);
            
            if(!this.playerInWildernessList.contains(playerUuidHash)) {
                player.sendMessage(this.message.getRegionWilderness());
                this.playerInWildernessList.add(playerUuidHash);
            }
            return false;
        }
        else { // Player on Plot

            if(this.playerInWildernessList.contains(playerUuidHash)) {
                player.sendMessage(this.message.getRegionPlot());
                this.playerInWildernessList.remove(playerUuidHash);
            }
            
            boolean isHelper =
                this.cacheHelper.hasPlayerThisHelper(
                    ownerUuidFromPlot,
                    playerUuid
                );
            
            boolean isBuildAllow =
                ownerUuidFromPlot.equals(playerUuid) ||
                isHelper ||
                player.isOp();

            if(!isBuildAllow) 
                player.sendMessage(this.message.getForbiddenBreak());
   
            return !isBuildAllow;
        }
    }
}
