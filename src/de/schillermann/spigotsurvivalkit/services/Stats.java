package de.schillermann.spigotsurvivalkit.services;

import de.schillermann.spigotsurvivalkit.databases.tables.StatsTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Mario Schillermann
 */
final public class Stats {
    
    final private StatsTable tableStats;
    
    final private StatsConfig config;
    
    public Stats(StatsTable tableStats, StatsConfig config) {
        this.tableStats = tableStats;
        this.config = config;
    }
    
    public List<String> getRichestPlayers() {
        
        List<String> richestPlayerList = new ArrayList<>();
        richestPlayerList.add(this.config.getHeadline());
        
        Map<UUID,Integer> playerStatsMap =
            this.tableStats.selectRichestPlayer(this.config.getNumber());
        
        if(playerStatsMap == null || playerStatsMap.isEmpty()) {
           
            richestPlayerList.add(this.config.getEmpty());
        }
        else {
            
            playerStatsMap.entrySet().stream().forEach((playerStats) -> {
                OfflinePlayer offlinePlayer =
                    Bukkit.getOfflinePlayer((UUID)playerStats.getKey());
                
                richestPlayerList.add(
                    this.config.getRow(
                        offlinePlayer.getName(),
                        playerStats.getValue()
                    )
                );
            });
        }
        richestPlayerList.add(this.config.getFooter());
        
        return richestPlayerList;
    }
}
