package de.schillermann.spigotsurvivalkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Mario Schillermann
 */
final public class SellCommand implements CommandExecutor {
    /*
    final ChunkProtectTable chunkProtectTable;
    final ChunkProtectCache chunkProtectCache;
    final ShopApi shopApi;
    final String msgSuccess;
    final String msgFailure;
    final String msgFullInventory;
    final int chunkPrice;
    
    public SellCommand(
        ChunkProtectTable chunkProtectTable,
        ChunkProtectCache chunkProtectCache,
        ShopApi shopApi,
        String msgSuccess,
        String msgFailure,
        String msgFullInventory,
        int chunkPrice
    ) {
        this.chunkProtectTable = chunkProtectTable;
        this.chunkProtectCache = chunkProtectCache;
        this.shopApi = shopApi;
        this.msgSuccess = msgSuccess;
        this.msgFailure = msgFailure;
        this.msgFullInventory = msgFullInventory;
        this.chunkPrice = chunkPrice;
    }
    */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         /*
        if(CommandUtils.noConsoleUsage(sender)) return true;
        
        boolean bought;
        Player player = (Player) sender;

        int chunkX = player.getLocation().getChunk().getX();
        int chunkZ = player.getLocation().getChunk().getZ();
        
        if(player.hasPermission("bukkitchunkrestoreprotect.free"))
        {
            bought = this.chunkProtectTable.deleteChunkProtect(chunkX, chunkZ);
            if(bought) {
                this.chunkProtectCache.clear();
                player.sendMessage(this.msgSuccess);
            }
            else {
                player.sendMessage(this.msgFailure);
            }
            return true;
        }
        
        int slot = 0;
        int inventorySize = player.getInventory().getSize();
        
        for(; slot < inventorySize; slot++) {

            if(player.getInventory().getItem(slot) == null) break;
        }

        if(slot == inventorySize) {
            player.sendMessage(this.msgFullInventory);
            return true;
        }
        
        bought = this.chunkProtectTable.deleteChunkProtect(chunkX, chunkZ);

        if(bought) {
            this.chunkProtectCache.clear();
            player.sendMessage(this.msgSuccess);
            
            ItemStack itemStack = new ItemStack(
                this.shopApi.getCurrency(),
                this.chunkPrice
            );
            player.getInventory().addItem(itemStack);
        }
        else {
            player.sendMessage(this.msgFailure);
        }*/
        return true;
    }
}
