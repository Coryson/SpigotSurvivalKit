package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.services.BankProvider;
import de.schillermann.spigotsurvivalkit.services.Stats;
import de.schillermann.spigotsurvivalkit.utils.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerListener implements Listener {
    
    final private BankProvider providerBank;
    
    final private Stats stats;
    
    final private JoinMessage message;
    
    final private RespawnWarp warpDeath;
    
    public PlayerListener(
        BankProvider providerBank,
        Stats stats,
        JoinMessage message,
        RespawnWarp warpDeath
    ) {
  
        this.providerBank = providerBank;
        this.stats = stats;
        this.message = message;
        this.warpDeath = warpDeath;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        Player player = event.getPlayer();

        if(player.hasPlayedBefore()) {
            int playerBalance = this.providerBank.getBalance(player.getUniqueId());

            if(playerBalance > 0) {
                
                boolean addedItem =
                    InventoryUtil.addItem(
                        player,
                        this.providerBank.GetCurrency(),
                        playerBalance
                    );
                
                if(addedItem) {
                    
                    this.providerBank.setBalance(player.getUniqueId(), 0);
                    player.sendMessage(this.message.getMoney(playerBalance));
                }
                else {
                    player.sendMessage(this.message.getInventoryFull());
                }
            }
        }
        else
            player.sendMessage(this.message.getFirst());
        
        String RichestPlayers = stats.getRichestPlayers();
        if(RichestPlayers != null)
            player.sendMessage(RichestPlayers);
        
        event.setJoinMessage(this.message.getNormally(player.getName()));
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
        
        if(this.warpDeath != null) {
            event.setRespawnLocation(this.warpDeath.getLocation());
            
            Player player = event.getPlayer();
            player.sendMessage(ChatColor.YELLOW + this.warpDeath.getMessage());
        }
    }
}
