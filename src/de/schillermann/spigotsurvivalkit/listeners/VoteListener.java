package de.schillermann.spigotsurvivalkit.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.schillermann.spigotsurvivalkit.services.BankProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


/**
 *
 * @author Mario Schillermann
 */
final public class VoteListener implements Listener {
    
    final private BankProvider providerBank;
    
    final private int votePrice;
    
    final private String msgThanks;
    
    final private String msgPlayerNotExist =
        "Received vote. Player name %s does not exist!";
    
    public VoteListener(
        BankProvider providerBank,
        int votePrice,
        String msgThanks
    ) {
        
        this.providerBank = providerBank;
        this.votePrice = votePrice;
        this.msgThanks = msgThanks;
    }
    
    @EventHandler
    public void onVote(VotifierEvent e) {
        
        Vote vote = e.getVote();
        String username = vote.getUsername();
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        
        if(player == null) {
            
            String errorMsg = String.format(this.msgPlayerNotExist, username);
            Bukkit.getLogger().warning(errorMsg);
            return;
        }
        
        if(this.providerBank.transfer(player.getUniqueId(), this.votePrice))
            Bukkit.broadcastMessage(
                String.format(
                    this.msgThanks,
                    username,
                    this.votePrice
                )
            );
    }
}
