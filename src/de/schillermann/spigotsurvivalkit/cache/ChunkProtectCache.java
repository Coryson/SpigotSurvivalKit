package de.schillermann.spigotsurvivalkit.cache;

import de.schillermann.spigotsurvivalkit.databases.tables.ChunkProtectTable;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkProtectCache {
    
    final ChunkProtectTable chunkProtectTable;
    final int cacheSize;
    UUID[] playerUuidList;
    Chunk[] chunkList;
    int index;
    
    public ChunkProtectCache(ChunkProtectTable chunkProtectTable, int cacheSize) {
        
        this.chunkProtectTable = chunkProtectTable;
        this.cacheSize = cacheSize;
        this.clear();
    }
    
    public void clear() {
        this.playerUuidList = new UUID[this.cacheSize];
        this.chunkList = new Chunk[this.cacheSize];
        this.index = 0;
    }
    
    public UUID getPlayerUuid(Chunk chunk) {
        
        int foundIndex = Arrays.asList(this.chunkList).indexOf(chunk);

        if(foundIndex == -1) {
     
            if(this.index == this.cacheSize) this.index = 0;
            
            foundIndex = this.index;
            this.index++;
            this.chunkList[foundIndex] = chunk;
            
            this.playerUuidList[foundIndex] =
                this.chunkProtectTable.selectPlayerUuidFromChunkProtect(chunk);
        }
        
        return this.playerUuidList[foundIndex];
    }
}
