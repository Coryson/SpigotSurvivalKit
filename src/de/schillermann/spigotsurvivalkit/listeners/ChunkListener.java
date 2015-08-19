package de.schillermann.spigotsurvivalkit.listeners;

import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkListener implements Listener {
/*
    final ChunkLogTable chunkTable;
    final ChunkProtectTable chunkProtectTable;
    final ChunkLogCache chunkLogCache;
    final ChunkProtectCache chunkProtectCache;
    final HelperCache helperCache;
    final HelperTable helperTable;
    final String forbiddenBreak;
    final String forbiddenBuild;
    
    public ChunkListener(
        ChunkLogTable chunkTable,
        ChunkProtectTable chunkProtectTable,
        ChunkLogCache chunkLogCache,
        ChunkProtectCache chunkProtectCache,
        HelperCache helperCache,
        HelperTable helperTable,
        String forbiddenBreak,
        String forbiddenBuild
    ) {
        this.chunkTable = chunkTable;
        this.chunkProtectTable = chunkProtectTable;
        this.chunkLogCache = chunkLogCache;
        this.chunkProtectCache = chunkProtectCache;
        this.helperCache = helperCache;
        this.helperTable = helperTable;
        this.forbiddenBreak = forbiddenBreak;
        this.forbiddenBuild = forbiddenBuild;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {

        if(event.isCancelled()) return;
        Player player = event.getPlayer();
        
        if(
            player == null ||
            player.getWorld().getEnvironment() != World.Environment.NORMAL ||
            player.hasPermission("bukkitchunkrestoreprotect.free")
        ) return;
        
        UUID playerUuid = player.getUniqueId();
        
        UUID playerUuidFromChunk =
            this.chunkProtectCache.getPlayerUuid(event.getBlock().getChunk());
        
        if(
            playerUuidFromChunk == null ||
            playerUuidFromChunk.equals(playerUuid) ||
            this.helperCache.hasPlayerThisHelper(playerUuidFromChunk, playerUuid)
        ) {
            return;
        }
        
        event.setCancelled(true);
        player.sendMessage(this.forbiddenBuild);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {

        if(event.isCancelled()) return;
        Player player = event.getPlayer();
        
        if(
            player == null ||
            player.getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        Block block = event.getBlock();
        if(block == null) return;
        
        Chunk chunk = block.getChunk();
        UUID playerUuid = player.getUniqueId();
        UUID playerUuidFromChunk =
            this.chunkProtectCache.getPlayerUuid(chunk);

        // Spieler darf abbauen wenn
        if(
            playerUuidFromChunk == null ||
            playerUuidFromChunk.equals(playerUuid) ||
            player.hasPermission("bukkitchunkrestoreprotect.free") ||
            this.helperCache.hasPlayerThisHelper(playerUuidFromChunk, playerUuid)
        ) {
            this.chunkLogCache.addChunk(chunk);
            return;
        }
        
        event.setCancelled(true);
        player.sendMessage(this.forbiddenBreak);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event){
        
        if(
            event.isCancelled() ||
            event.getLocation().getWorld().getEnvironment() != World.Environment.NORMAL
        ) return;
        
        Chunk chunk = event.getLocation().getChunk();

        if(this.chunkProtectTable.isAdjacentChunkProtect(chunk)) {
            event.setCancelled(true);
            return;
        }
        
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
	int endX = chunkX + 1;
	int endZ = chunkZ + 1;
	int counterZ;

	// Speichert umliegende Chunks mit
	for(int counterX = chunkX - 1; counterX <= endX; counterX++) {
            for(counterZ = chunkZ - 1; counterZ <= endZ; counterZ++) {
                this.chunkTable.insertChunk(counterX, counterZ);
            }
        }
    }*/
}
