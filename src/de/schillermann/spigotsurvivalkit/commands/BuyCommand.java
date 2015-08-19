package de.schillermann.spigotsurvivalkit.commands;

import de.schillermann.spigotsurvivalkit.cache.ChunkProtectCache;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkProtectTable;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Mario Schillermann
 */
final public class BuyCommand implements CommandExecutor {
    
    final ChunkProtectTable chunkProtect;
    final ChunkProtectCache chunkProtectCache;
    final Material currency;
    final int chunkPrice;
    final String msgSuccess;
    final String msgBroke;
    final String msgFailure;
    final String msgForbidden;
    
    public BuyCommand(
        ChunkProtectTable chunkProtect,
        ChunkProtectCache chunkProtectCache,
        Material currency,
        int chunkPrice,
        String msgSuccess,
        String msgBroke,
        String msgFailure,
        String msgForbidden
    ) {
        this.chunkProtect = chunkProtect;
        this.chunkProtectCache = chunkProtectCache;
        this.currency = currency;
        this.chunkPrice = chunkPrice;
        this.msgSuccess = msgSuccess;
        this.msgBroke = msgBroke;
        this.msgFailure = msgFailure;
        this.msgForbidden = msgForbidden;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         
        if(!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        
        if(player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(this.msgForbidden);
            return true;
        }
                
        ItemStack[] inventory =  player.getInventory().getContents();

        for(int slot = 0; slot < inventory.length; slot++) {

            if(
                inventory[slot] != null &&
                inventory[slot].getType() == this.currency &&
                inventory[slot].getAmount() >= this.chunkPrice
            ) {
                int newAmount = inventory[slot].getAmount() - this.chunkPrice;

                if(newAmount == 0)
                    player.getInventory().clear(slot);
                else
                    inventory[slot].setAmount(newAmount);
                
                this.buyChunk(player);
                
                
                return true;
            }
                
        }
        player.sendMessage(this.msgBroke);
        
        return true;
    }
    
    void buyChunk(Player player) {
        
        Chunk chunk = player.getLocation().getChunk();
        
        boolean bought = this.chunkProtect.insertChunkProtect(
            chunk.getX(),
            chunk.getZ(),
            player.getUniqueId()
        );
        
        if(bought) {
            
            player.sendMessage(this.msgSuccess);
        }
        else
            player.sendMessage(this.msgFailure);
    }
}
