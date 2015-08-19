package de.schillermann.spigotsurvivalkit.cache;

import de.schillermann.spigotsurvivalkit.databases.tables.ChunkLogTable;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkProtectTable;
import java.util.Arrays;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkLogCache {
    
    final ChunkLogTable chunkLog;
    final ChunkProtectTable chunkProtect;
    final Chunk[] cache;
    final int cacheSize;
    int index = 0;
    
    public ChunkLogCache(
        ChunkLogTable chunkLog,
        ChunkProtectTable chunkProtect,
        int cacheSize
    ) {
        this.chunkLog = chunkLog;
        this.chunkProtect = chunkProtect;
        this.cacheSize = cacheSize;
        this.cache = new Chunk[this.cacheSize];
    }
    
    public void addChunk(Chunk chunk) {
        
        if(Arrays.asList(this.cache).contains(chunk)) return;
        
        if(this.chunkProtect.selectPlayerUuidFromChunkProtect(chunk) == null)
            this.chunkLog.insertChunk(chunk.getX(), chunk.getZ());
        
        if(this.index == this.cacheSize) this.index = 0;

        this.cache[this.index] = chunk;
        this.index++;
    }
}
