package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.services.PlotProvider;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotCommand implements CommandExecutor {
    
    final private PlotProvider providerPlot;
    
    final private PlotCommandMessage message;
    
    public PlotCommand(PlotProvider providerPlot, PlotCommandMessage message) {
        
        this.providerPlot = providerPlot;
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
        Chunk chunk = player.getLocation().getChunk();
        
        if(this.providerPlot.releasePlot(chunk))
            player.sendMessage(this.message.getReleaseSuccess());
        else
            player.sendMessage(this.message.getReleaseError());
        
        return true;
    }
}
