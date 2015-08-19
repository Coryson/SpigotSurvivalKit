package de.schillermann.spigotsurvivalkit.commands;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkCommand implements CommandExecutor {
/*    
    final ChunkLogTable chunkTable;
    final ChunkProtectTable chunkProtectTable;
    final String msgChunkError;
    final String msgChunkSuccess;
    final String msgChunkBought;
    final String msgChunksSuccess;
    final String msgChunksError;
    
    public ChunkCommand(
        ChunkLogTable chunkTable,
        ChunkProtectTable chunkProtectTable,
        String msgChunkError,
        String msgChunkSuccess,
        String msgChunkBought,
        String msgChunksSuccess,
        String msgChunksError
    ) {
        this.chunkTable = chunkTable;
        this.chunkProtectTable = chunkProtectTable;
        this.msgChunkBought = msgChunkBought;
        this.msgChunkError = msgChunkError;
        this.msgChunkSuccess = msgChunkSuccess;
        this.msgChunksSuccess = msgChunksSuccess;
        this.msgChunksError = msgChunksError;
    }
    */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*
        if(args.length < 1) return false;
        if(CommandUtils.noConsoleUsage(sender)) return true;
        
        Player player = (Player) sender;
        if(!player.hasPermission("bukkitchunkrestoreprotect.chunk")) return true;
        
        boolean result;
        
        switch(args[0]) {
            case "regen":
                
                switch(args.length) {
                    case 1:
                        UUID playerUuid = this.chunkProtectTable.selectPlayerUuidFromChunkProtect(
                            player.getLocation().getChunk()
                        );
                        
                        if(playerUuid != null) {
                            
                            OfflinePlayer offlinePlayer =
                                Bukkit.getOfflinePlayer(playerUuid);
                            
                            player.sendMessage(
                                String.format(
                                    this.msgChunkBought,
                                    offlinePlayer.getName()
                                )
                            );
                            return true;
                        }
                        
                        result = Bukkit.getWorld("world").regenerateChunk(
                            player.getLocation().getChunk().getX(),
                            player.getLocation().getChunk().getZ()
                        );
                        if(result) {
                            this.chunkTable.deleteChunk(
                                player.getLocation().getChunk()
                            );
                            player.sendMessage(this.msgChunkSuccess);
                            return true;
                        }
                        
                        player.sendMessage(this.msgChunkError);
                        return true;
                    case 2:
                        int restoredChunks = 0;
                        Chunk chunk;
                        
                        List<ChunkLocation> chunkList =
                            this.chunkProtectTable.selectUnprotectChunks();

                        for(ChunkLocation chunkLocation : chunkList) {
                            
                            chunk =  Bukkit.getWorld("world").getChunkAt(
                                chunkLocation.getX(),
                                chunkLocation.getZ()
                            );
                            
                            result = Bukkit.getWorld("world").regenerateChunk(
                                chunk.getX(),
                                chunk.getZ()
                            );
                            
                            if(result) {
                                this.chunkTable.deleteChunk(chunk);
                                restoredChunks++;
                                continue;
                            }
                            
                            player.sendMessage(
                                String.format(
                                    this.msgChunksError,
                                    chunkLocation.getX(),
                                    chunkLocation.getZ()
                                )
                            );
                        }
                        player.sendMessage(
                            String.format(this.msgChunksSuccess, restoredChunks)
                        );
                        return true;
                }
        }
        return false;*/
        return true;
    }
}
