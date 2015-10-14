package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.menu.WarpsMenu;
import java.util.Arrays;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class SetWarpCommand implements CommandExecutor {
    
    final private WarpTable tableWarp;
    
    final private SetWarpCommandMessage message;
    
    final private WarpsMenu menuWarps;
    
    public SetWarpCommand(
        WarpTable tableWarp,
        SetWarpCommandMessage message,
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
        if(args.length < 2 || !(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        World world = player.getLocation().getWorld();
        String warpName = args[0];
        String errorMsg = this.message.getSetWarpError(warpName);
        
        if(world.getEnvironment() != Environment.NORMAL) {
            player.sendMessage(errorMsg);
            return true;
        }
        
        boolean createdWarp = this.tableWarp.insertWarp(
            warpName,
            convertWarpDescription(args),
            player.getItemInHand().getType(),
            world.getUID(),
            player.getLocation().getX(),
            player.getLocation().getY(),
            player.getLocation().getZ(),
            player.getUniqueId()
        );
        
        if(createdWarp) {
            
            this.menuWarps.reloadInventory();
            player.sendMessage(this.message.getSetWarpSuccess(warpName));
        }
        else {
            player.sendMessage(errorMsg);
        }
        
        return true;
    }
    
    private static String convertWarpDescription(String[] descriptionLines) {
        
        if(descriptionLines.length < 2)
            return "";
        
        String[] descriptionWithoutWarpName =
            Arrays.copyOfRange(descriptionLines, 1, descriptionLines.length);
        
        return String.join(" ", descriptionWithoutWarpName);
    }
}
