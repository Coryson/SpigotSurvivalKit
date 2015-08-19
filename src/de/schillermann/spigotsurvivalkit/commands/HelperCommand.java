package de.schillermann.spigotsurvivalkit.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class HelperCommand implements CommandExecutor {
    /*
    final HelperTable helperTable;
    final HelperCache helperCache;
    final String msgAdd;
    final String msgRemove;
    final String msgList;
    final String msgOther;
    final String msgPlayerOnline;
    
    public HelperCommand(
        HelperTable helperTable,
        HelperCache helperCache,
        String msgAdd,
        String msgRemove,
        String msgList,
        String msgOther,
        String msgPlayerOnline
    ) {
        this.helperTable = helperTable;
        this.helperCache = helperCache;
        this.msgAdd = msgAdd;
        this.msgRemove = msgRemove;
        this.msgList = msgList;
        this.msgOther = msgOther;
        this.msgPlayerOnline = msgPlayerOnline;
    }
    */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         /*
        if(CommandUtils.noConsoleUsage(sender)) return true;
        
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        
        if(args.length > 0) {
            
            String helperName = args[0];
            UUID helperUuid = Bukkit.getOfflinePlayer(helperName).getUniqueId();
            
            if(this.helperTable.deleteHelper(playerUuid, helperUuid)) {
                
                this.helperCache.clear();
                player.sendMessage(String.format(this.msgRemove, helperName));
                return true;
            }
            
            Player helper = player.getServer().getPlayer(helperName);
            if(helper == null) {
                player.sendMessage(String.format(this.msgPlayerOnline, helperName));
                return true;
            }
            
            if(this.helperTable.insertHelper(playerUuid, helperUuid)) {
                
                this.helperCache.clear();
                String playerName = Bukkit.getPlayer(playerUuid).getName();
                
                player.sendMessage(String.format(this.msgAdd, helperName));
                helper.sendMessage(String.format(this.msgOther, playerName));
                
                return true;
            }
            
            return true;
        }
        
        List<UUID> helperList =
            this.helperTable.selectHelperListFromPlayer(playerUuid);
        
        player.sendMessage(
            String.format(
                this.msgList,
                helperList.size(),
                this.GetPlayerNameFromUuid(helperList)
            )
        );
        
        return true;
    }
    
    String GetPlayerNameFromUuid(List<UUID> playerUuidList) {
        
        List<String> playerNameUuidList = new ArrayList<>();
        
        for (UUID playerUuid : playerUuidList) {
            playerNameUuidList.add(Bukkit.getOfflinePlayer(playerUuid).getName());
}
        return playerNameUuidList.toString();*/
        return true;
    }
            
}
