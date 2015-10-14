package de.schillermann.spigotsurvivalkit.utils;

import de.schillermann.spigotsurvivalkit.databases.DatabaseProvider;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.BlockLocation;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

/**
 *
 * @author Mario Schillermann
 */
final public class StatsUpdate implements Runnable {
   
    final private String pluginName;
    
    final private DatabaseProvider database;
    
    final private Material currency;
    
    final private int plotDefaultPrice;
    
    final private List<String> blacklist;
    
    final private String message;
    
    public StatsUpdate(
        String pluginName,
        DatabaseProvider database,
        Material currency,
        int plotDefaultPrice,
        List<String> blacklist,
        String message
    ) {
        
        this.pluginName = pluginName;
        this.database = database;
        this.currency = currency;
        this.plotDefaultPrice = plotDefaultPrice;
        this.blacklist = blacklist;
        this.message = message;
    }
    
    @Override
    public void run() {
        
        Material materialChest = Material.CHEST;
        
        this.database.getTableStats().clearTable();
        
        HashMap<UUID, Integer> rangList =
            this.database.getTablePlot().selectPlotQuantity();
 
        rangList.entrySet().stream().forEach((playerRang) -> {
            
            UUID playerUuid = playerRang.getKey();
            
            if(!this.blacklist.contains(playerUuid.toString())) {
            
                Integer balance = playerRang.getValue() * plotDefaultPrice;

                List<BlockLocation> blockLocationList =
                    this.database.getTableLock().selectLockLocationFromPlayer(
                        playerUuid,
                        materialChest
                    );

                for(BlockLocation blockLocation : blockLocationList) {

                    Location chestLocation = new Location(
                        Bukkit.getWorld(blockLocation.getWorld()),
                        blockLocation.getX(),
                        blockLocation.getY(),
                        blockLocation.getZ()
                    );

                    Block block = chestLocation.getBlock();

                    if(block == null || block.getType() != materialChest) {

                        this.database.getTableLock().deleteLock(
                            new BlockLocation(chestLocation)
                        );
                        continue;
                    }

                    Chest chest = (Chest)block.getState();
                    balance +=
                        InventoryUtil.getAmountsOfItem(
                            chest.getBlockInventory(),
                            currency
                        );
                }

                if(!this.database.getTableStats().updateStats(playerUuid, balance))
                    this.database.getTableStats().insertStats(playerUuid, balance);
            }
        });
        
        Bukkit.getLogger().info(String.format(this.message, this.pluginName));
    }
}
