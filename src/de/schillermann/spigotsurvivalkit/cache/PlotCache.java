package de.schillermann.spigotsurvivalkit.cache;

import de.schillermann.spigotsurvivalkit.databases.tables.PlotTable;
import de.schillermann.spigotsurvivalkit.entities.PlotMetadata;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotCache {
    
    final private PlotTable plot;
    final private int cacheSize;
    private UUID[] ownerUuidFromPlotList;
    private Chunk[] chunkList;
    private int index;
    
    public PlotCache(PlotTable plot, int cacheSize) {
        
        this.plot = plot;
        this.cacheSize = cacheSize;
        this.clear();
    }
    
    public void clear() {
        this.ownerUuidFromPlotList = new UUID[this.cacheSize];
        this.chunkList = new Chunk[this.cacheSize];
        this.index = 0;
    }
    
    public UUID getOwnerUuidFromPlot(Chunk chunk) {
        
        int foundIndex = Arrays.asList(this.chunkList).indexOf(chunk);
        
        if(foundIndex == -1) {
     
            if(this.index == this.cacheSize) this.index = 0;
            
            foundIndex = this.index;
            this.index++;
            this.chunkList[foundIndex] = chunk;
            
            UUID plotOwnerUuid = null;
            PlotMetadata metadata = this.plot.selectMetadata(chunk);
            
            if(metadata != null)
                plotOwnerUuid = metadata.getOwner();
            
            this.ownerUuidFromPlotList[foundIndex] = plotOwnerUuid;
        }

        return this.ownerUuidFromPlotList[foundIndex];
    }
}
