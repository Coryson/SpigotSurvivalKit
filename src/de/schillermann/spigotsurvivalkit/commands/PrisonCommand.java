package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class PrisonCommand implements CommandExecutor {
    
    final private Location location;
    
    final private String msgBroadcast;
    
    final private String msgPlayerNotFound;
    
    public PrisonCommand(
        Location location,
        String msgBroadcast,
        String msgPlayerNotFound
    ) {
        this.location = location;
        this.msgBroadcast = msgBroadcast;
        this.msgPlayerNotFound = msgPlayerNotFound;
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
        
        String cheatName = args[0];
        Player cheat = Bukkit.getPlayer(cheatName);
        if(cheat == null) {
            player.sendMessage(
                String.format(this.msgPlayerNotFound, cheatName)
            );
            return true;
        }
            
        cheat.teleport(location);
        Bukkit.broadcastMessage(this.msgBroadcast);
        
        return true;
    }
}
