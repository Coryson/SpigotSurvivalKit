package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.services.BankProvider;
import de.schillermann.spigotsurvivalkit.services.Stats;
import de.schillermann.spigotsurvivalkit.utils.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerListener implements Listener {
    
    final private BankProvider providerBank;
    
    final private Stats stats;
    
    final private Hospital deathSpawn;
    
    final private JoinMessage message;
    
    public PlayerListener(
        BankProvider providerBank,
        Stats stats,
        Hospital deathSpawn,
        JoinMessage message) {
  
        this.providerBank = providerBank;
        this.stats = stats;
        this.deathSpawn = deathSpawn;
        this.message = message;
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        
        Player player = event.getEntity();
        player.teleport(this.deathSpawn.getLocation());
        player.sendMessage(this.deathSpawn.getinfo());
    }
}
