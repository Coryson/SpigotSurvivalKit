package de.schillermann.spigotsurvivalkit.services;

import de.schillermann.spigotsurvivalkit.cache.PlotCache;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkLogTable;
import de.schillermann.spigotsurvivalkit.databases.tables.PlotTable;
import de.schillermann.spigotsurvivalkit.entities.PlotMetadata;
import de.schillermann.spigotsurvivalkit.utils.InventoryUtil;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotProvider {
    
    final private PlotTable tablePlot;
    
    final private PlotCache cachePlot;
    
    final private ChunkLogTable tableChunkLog;
    
    final private BankProvider providerBank;
    
    final private int plotPriceDefault;

    public PlotProvider(
        PlotTable tablePlot,
        PlotCache cachePlot,
        ChunkLogTable tableChunkLog,
        BankProvider providerBank,
        int plotPriceDefault
    ) {
        
        this.tablePlot = tablePlot;
        this.cachePlot = cachePlot;
        this.tableChunkLog = tableChunkLog;
        this.providerBank = providerBank;
        this.plotPriceDefault = plotPriceDefault;
    }
            
    public PlotMetadata getPlotMetadata(Chunk chunk) {
        
        return this.tablePlot.selectMetadata(chunk);
    }
    
    public boolean buyPlot(Player buyer) {
        
        int plotPrice = this.plotPriceDefault;
        UUID buyerUuid = buyer.getUniqueId();
        Chunk chunk = buyer.getLocation().getChunk();
        
        PlotMetadata metadata = this.tablePlot.selectMetadata(chunk);
        
        if(metadata != null) plotPrice = metadata.getPrice();
        
        boolean paid = InventoryUtil.removeItem(
            buyer,
            this.providerBank.GetCurrency(),
            plotPrice
        );
        
        if(!paid) return false;
        
        this.cachePlot.clear();
        this.tableChunkLog.deleteChunk(chunk);
        
        if(metadata == null) // Plot buy from Bank
            return this.tablePlot.insertPlot(chunk, buyerUuid);
            
        // Plot buy from Player
        OfflinePlayer owner = Bukkit.getOfflinePlayer(metadata.getOwner());
        this.providerBank.transfer(owner.getUniqueId(), plotPrice);
        
        return this.tablePlot.updateMetadata(chunk, new PlotMetadata(buyerUuid, 0));
    }
    
    public boolean sellPlot(Chunk chunk, int price) {

        return this.tablePlot.updatePrice(chunk, price);
    }
    
    public boolean sellPlotCancel(Chunk chunk) {
        return this.tablePlot.updatePrice(chunk, 0);
    }
    
    public boolean releasePlot(Chunk chunk) {
        
        if(this.tablePlot.deletePlot(chunk)) {
            this.cachePlot.clear();
            return true;
        }
        
        return false;
    }
    
    public int getPlotPriceDefault() {
        return this.plotPriceDefault;   
    }
}
