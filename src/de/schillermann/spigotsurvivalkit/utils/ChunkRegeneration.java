package de.schillermann.spigotsurvivalkit.utils;

import de.schillermann.spigotsurvivalkit.databases.tables.ChunkLogTable;
import de.schillermann.spigotsurvivalkit.entities.ChunkLocation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkRegeneration implements Runnable {
    
    final private static String WORLD_NAME = "world";
    
    final private String pluginName;
    
    final private ChunkLogTable chunkLog;
    
    final private String msgChunksSuccess;
    
    final private String msgChunksError;
    
    public ChunkRegeneration(
        String pluginName,
        ChunkLogTable chunkLog,
        String msgChunksSuccess,
        String msgChunksError
    ) {
        this.pluginName = pluginName;
        this.chunkLog = chunkLog;
        this.msgChunksSuccess = msgChunksSuccess;
        this.msgChunksError = msgChunksError;
    }
    
    @Override
    public void run() {
        int restoredChunks = 0;
        
        for(ChunkLocation chunkLocation : this.chunkLog.selectChunks()) {

            Chunk chunk =  Bukkit.getWorld(WORLD_NAME).getChunkAt(
                chunkLocation.getX(),
                chunkLocation.getZ()
            );

            boolean result = Bukkit.getWorld(WORLD_NAME).regenerateChunk(
                chunk.getX(),
                chunk.getZ()
            );

            if(result) {
                this.chunkLog.deleteChunk(chunk);
                restoredChunks++;
                continue;
            }

            Bukkit.getLogger().warning(
                String.format(
                    this.msgChunksError,
                    this.pluginName,
                    chunkLocation.getX(),
                    chunkLocation.getZ()
                )
            );
        }
        
        Bukkit.getLogger().info(
            String.format(
                this.msgChunksSuccess,
                this.pluginName,
                restoredChunks
            )
        );
    }
}
