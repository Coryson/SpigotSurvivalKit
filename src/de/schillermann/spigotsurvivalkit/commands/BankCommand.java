package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.services.BankProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class BankCommand implements CommandExecutor {
    
    final private BankProvider bank;
    
    final private BankCommandMessage message;
    
    public BankCommand(BankProvider bank, BankCommandMessage message) {
        this.bank = bank;
        this.message = message;
    }
    
    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if(args.length == 0) return false;
        
        Player player = (Player) sender;
        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[0]);

        if(receiver == null || !receiver.hasPlayedBefore()) {
            player.sendMessage(this.message.getPlayerNotFound());
        }
        else if(args.length == 1) {
            
            int balance = this.bank.getBalance(receiver.getUniqueId());
            player.sendMessage(this.message.getBalance(balance));
        }
        else if(args.length == 2) {
            
            int price = 0;

            try {  
                price = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e) {
                return false;
            }
            
            if(price < 1) return false;
        
            if(this.bank.transfer(receiver.getUniqueId(), price))
                
                player.sendMessage(
                    this.message.getTransfer(receiver.getName(), price)
                );
        }
        
        return true;
    }
}
