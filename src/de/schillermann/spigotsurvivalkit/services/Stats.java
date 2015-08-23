package de.schillermann.spigotsurvivalkit.services;

import de.schillermann.spigotsurvivalkit.databases.tables.StatsTable;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Mario Schillermann
 */
final public class Stats {
    
    final private StatsTable table;
    
    final private StatsConfig config;
    
    public Stats(StatsTable table, StatsConfig config) {
        this.table = table;
        this.config = config;
    }
    
    public String getRichestPlayers() {
        
        StringBuilder message = new StringBuilder();
        Map<UUID,Integer> playerStatsMap =
            this.table.selectRichestPlayer(this.config.getNumber());
        
        if(playerStatsMap != null && playerStatsMap.size() > 0) {
            
            message.append(this.config.getHeadline());
            
            for(Map.Entry playerStats : playerStatsMap.entrySet()) {
                
                OfflinePlayer offlinePlayer =
                    Bukkit.getOfflinePlayer((UUID)playerStats.getKey());
                
                message.append(
                    this.config.getRow(
                        offlinePlayer.getName(),
                        (Integer)playerStats.getValue()
                    )
                );
            }
            return message.toString();
        }
        else {
            return null;
        }
    }
}
