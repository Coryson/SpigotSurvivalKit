package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.menu.WarpsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class RemoveWarpCommand implements CommandExecutor {
    
    final private WarpTable tableWarp;
    
    final private RemoveWarpCommandMessage message;
    
    final private WarpsMenu menuWarps;
    
    public RemoveWarpCommand(
        WarpTable tableWarp,
        RemoveWarpCommandMessage message,
        WarpsMenu menuWarps
    ) {
        
        this.tableWarp = tableWarp;
        this.message = message;
        this.menuWarps = menuWarps;
    }
    
    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if(args.length == 0 || !(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        String warpName = args[0];
        
        if(this.tableWarp.deleteWarp(warpName)) {
            
            this.menuWarps.reloadInventory();
            player.sendMessage(this.message.getRemoveWarpSuccess(warpName));
        }
        else {
            player.sendMessage(this.message.getRemoveWarpError(warpName));
        }
        
        return true;
    }
}
