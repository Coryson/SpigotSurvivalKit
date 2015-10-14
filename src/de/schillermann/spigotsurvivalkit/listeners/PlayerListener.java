package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.services.BankProvider;
import de.schillermann.spigotsurvivalkit.services.Stats;
import de.schillermann.spigotsurvivalkit.utils.InventoryUtil;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    
    final private DefaultWarps warps;
    
    public PlayerListener(
        BankProvider providerBank,
        Stats stats,
        JoinMessage message,
        DefaultWarps warps
    ) {
  
        this.providerBank = providerBank;
        this.stats = stats;
        this.message = message;
        this.warps = warps;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        Player player = event.getPlayer();

        if(player.hasPlayedBefore()) {
            
            this.hasMoneyForPlayer(player);
        }
        else if(this.warps.getWarpFirstJoin() != null) {
            
            player.teleport(this.warps.getWarpFirstJoin().getLocation());
            String border =
                ChatColor.GOLD +
                "***************************************************";
            
            player.sendMessage(border);
            player.sendMessage(
                ChatColor.YELLOW +
                this.warps.getWarpFirstJoin().getMessage()
            );
            player.sendMessage(border);
        }
        
        List<String> richestPlayers = this.stats.getRichestPlayers();
        
        richestPlayers.stream().forEach((richestPlayer) -> {
            player.sendMessage(richestPlayer);
        });
        
        event.setJoinMessage(this.message.getNormally(player.getName()));
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
       
        if(this.warps.getWarpRespawn() != null) {
            
            event.setRespawnLocation(this.warps.getWarpRespawn().getLocation());
            
            Player player = event.getPlayer();
            player.sendMessage(
                ChatColor.YELLOW +
                this.warps.getWarpRespawn().getMessage()
            );
        }
    }
    
    private void hasMoneyForPlayer(Player player) {
        
        int playerBalance = this.providerBank.getBalance(player.getUniqueId());

        if(playerBalance == 0) return;

        boolean isAddedItem =
            InventoryUtil.addItem(
                player,
                this.providerBank.GetCurrency(),
                playerBalance
            );

        if(isAddedItem) {

            this.providerBank.setBalance(player.getUniqueId(), 0);
            player.sendMessage(this.message.getMoney(playerBalance));
        }
        else {
            player.sendMessage(this.message.getInventoryFull());
        }
    }
}
