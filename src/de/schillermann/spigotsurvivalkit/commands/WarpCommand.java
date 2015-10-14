package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.databases.tables.WarpTable;
import de.schillermann.spigotsurvivalkit.menu.WarpsMenu;
import java.util.Arrays;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class WarpCommand implements CommandExecutor {
    
    final private WarpTable tableWarp;
    
    final private WarpsMenu menuWarps;
    
    final private WarpCommandMessage message;
    
    public WarpCommand(
        WarpTable tableWarp,
        WarpsMenu menuWarps,
        WarpCommandMessage message
    ) {
       
        this.tableWarp = tableWarp;
        this.menuWarps = menuWarps;
        this.message = message;
    }
    
    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        
        if(args.length < 2 || !(sender instanceof Player)) return false;
        
        String action = args[0];
        
        if(!action.equals("set") && !action.equals("remove")) return false;
        
        Player player = (Player) sender;
        String warpName = args[1];
        
        if(action.equals("set")) {
            
            if(this.setWarp(warpName, convertWarpDescription(args), player))
                player.sendMessage(this.message.getSetWarpSuccess(warpName));
            else
                player.sendMessage(this.message.getSetWarpError(warpName));
        }
        else {
            if(this.removeWarp(warpName))
                player.sendMessage(this.message.getRemoveWarpSuccess(warpName));
            else
                player.sendMessage(this.message.getRemoveWarpError(warpName));
        }
        
        return true;
    }
    
    private boolean setWarp(String warpName, String warpDescription, Player player) {
        
        World world = player.getLocation().getWorld();
        
        if(world.getEnvironment() != World.Environment.NORMAL)
            return false;
        
        boolean isCreatedWarp = this.tableWarp.insertWarp(
            warpName,
            warpDescription,
            player.getItemInHand().getType(),
            world.getUID(),
            player.getLocation().getX(),
            player.getLocation().getY(),
            player.getLocation().getZ(),
            player.getUniqueId()
        );
        
        if(isCreatedWarp) this.menuWarps.reloadInventory();
        
        return isCreatedWarp;
    }
    
    private static String convertWarpDescription(String[] descriptionLines) {
        
        if(descriptionLines.length < 3) return "";
        
        String[] cutDescription =
            Arrays.copyOfRange(descriptionLines, 2, descriptionLines.length);
        
        return String.join(" ", cutDescription);
    }
    
    private boolean removeWarp(String warpName) {
        
        boolean isRemovedWarp = this.tableWarp.deleteWarp(warpName);
        if(isRemovedWarp) this.menuWarps.reloadInventory();
         
        return isRemovedWarp;
    }
}
