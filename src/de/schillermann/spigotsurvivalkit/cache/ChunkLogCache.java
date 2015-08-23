package de.schillermann.spigotsurvivalkit.cache;

import com.google.common.primitives.Ints;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkLogTable;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkLogCache {
    
    final private ChunkLogTable chunkLog;
    final private int[] cache;
    final private int cacheSize;
    private int index = 0;
    
    public ChunkLogCache(
        ChunkLogTable chunkLog,
        int cacheSize
    ) {
        this.chunkLog = chunkLog;
        this.cacheSize = cacheSize;
        this.cache = new int[this.cacheSize];
    }
    
    public void addChunk(Chunk chunk) {

        if(Ints.contains(this.cache, chunk.hashCode())) return;
        if(this.index == this.cacheSize) this.index = 0;

        this.chunkLog.insertChunk(chunk.getX(), chunk.getZ());
        
        this.cache[this.index] = chunk.hashCode();
        this.index++;
    }
}
