package de.schillermann.spigotsurvivalkit.menu;

import de.schillermann.spigotsurvivalkit.services.PlotProvider;
import de.schillermann.spigotsurvivalkit.cache.HelperCache;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenu {
    
    final private Plugin plugin;
    
    final private PlayerMenuItemPalette itemPalette;
    
    final private PlayerMenuMessage message;
    
    final private PlotProvider plot;
    
    final private HelperCache helperCache;
    
    /**
     * <player uuid hashcode, selected material>
     */
    final private HashMap<Integer, Material> needMetadata;
    
    public PlayerMenu(
        Plugin plugin,
        PlayerMenuItemPalette itemPalette,
        PlayerMenuMessage message,
        PlotProvider plot,
        HelperCache helperCache
    ) {
        this.plugin = plugin; 
        this.itemPalette = itemPalette;
        this.message = message;
        this.plot = plot;
        this.helperCache = helperCache;
        this.needMetadata = new HashMap<>();
    }
    
    public Inventory getMenu(Chunk chunk, UUID player) {
        
        ItemStack[] menuItems = new ItemStack[4];
        
        menuItems[0] = this.getPlotItem(chunk, player);
        menuItems[1] = this.getHelperItem(player);
        menuItems[2] = this.itemPalette.getTeleportToSpawn();
        menuItems[3] = this.itemPalette.getTeleportToBed();
        
        Inventory menu = Bukkit.createInventory(
            new PlayerMenuHolder(),
            9,
            this.message.getMenuTitle()
        );
        menu.addItem(menuItems);
        
        return menu;
    }
    
    public void selectMenuItem(Player player, Material select) {
        
        final UUID playerUuid = player.getUniqueId();
        final int playerUuidHash = playerUuid.hashCode();
        final Chunk chunk = player.getLocation().getChunk();
        final PlotMetadata metadata = this.plot.getPlotMetadata(chunk);
        boolean isPlayerOwner = false;
        int plotPrice = 1;
        
        if(metadata != null) {
            isPlayerOwner = metadata.getOwner() == playerUuid;
            plotPrice = metadata.getPrice();
        }
        
        if(this.itemPalette.isHelper(select) && isPlayerOwner) {
            
            player.sendMessage(this.message.getHelperName());
            
            this.needMetadata.remove(playerUuidHash);
            this.needMetadata.put(playerUuidHash, select);
        }
        else if(this.itemPalette.isPlotBuy(select) && plotPrice > 0) {
            
            if(this.plot.buyPlot(player))
                player.sendMessage(this.message.getPlotBuySuccess());
            else
                player.sendMessage(this.message.getPlotBuyBroke());
        }
        else if(this.itemPalette.isPlotSellCancel(select) && isPlayerOwner) {
            
            if(this.plot.sellPlotCancel(chunk))
                player.sendMessage(this.message.getPlotSellCancelSuccess());
        }
        else if(this.itemPalette.isPlotSell(select) && isPlayerOwner) {
            
            player.sendMessage(this.message.getPlotSellPriceInput());
            
            this.needMetadata.remove(playerUuidHash);
            this.needMetadata.put(playerUuid.hashCode(), select);
        }
        else if(this.itemPalette.isTeleportToBed(select)) {
            
            Location bed = player.getBedSpawnLocation();
            if (bed == null) {
                player.sendMessage(this.message.getNoBedSpawn());
            } else {
                player.teleport(player.getBedSpawnLocation());
            }
        }
        else if(this.itemPalette.isTeleportToSpawn(select)) {
            player.teleport(player.getWorld().getSpawnLocation());
        }

        Bukkit.getScheduler().runTask(
            this.plugin,
            new Runnable() {
                @Override
                public void run() {
                   
                    Player p = Bukkit.getPlayer(playerUuid);
                    if (p != null) p.closeInventory();
                }
            }
        );
    }
    
    public boolean isMetadata(Player player, String chatMessage) { 
        
        int playerUuidHash = player.getUniqueId().hashCode();
        if(!this.needMetadata.containsKey(playerUuidHash)) return false;
        
        Material selectMaterial = this.needMetadata.get(playerUuidHash);
        this.needMetadata.remove(playerUuidHash);
        
        if(this.itemPalette.isHelper(selectMaterial)) {
            this.editHelperFromPlayer(player, chatMessage);
            return true;
        }
        else if(this.itemPalette.isPlotSell(selectMaterial)) {
            
            int price = 0;

            try {  
                price = Integer.parseInt(chatMessage);
            }
            catch(NumberFormatException e) {}  
            
            if(
                price > 0 &&
                this.plot.sellPlot(player.getLocation().getChunk(), price)
            )

                player.sendMessage(this.message.getPlotSellRelease(price));
            else
                player.sendMessage(this.message.getPlotSellPriceError());
            
            return true;
        }
        
        return false;
    }
    
    private ItemStack getPlotItem(Chunk chunk, UUID player) {
        
        PlotMetadata metadata = this.plot.getPlotMetadata(chunk);
        
        if(metadata == null) {
            return this.itemPalette.getPlotBuy("Bank", 1);
        }
            
        UUID owner = metadata.getOwner();
        int plotPrice = metadata.getPrice();
        OfflinePlayer offlineOwner = Bukkit.getOfflinePlayer(metadata.getOwner());
        String ownerName = offlineOwner.getName();
        
        boolean isPlayerOwner =
            owner.hashCode() == player.hashCode();
        
        if(plotPrice == 0) {
            if(isPlayerOwner) { // Display Plot sell
                return this.itemPalette.getPlotSell();
            }
            else { // Display Plot Owner
                boolean isPlayerHelper =
                    this.helperCache.hasPlayerThisHelper(owner, player);
                return this.itemPalette.getPlotOwner(ownerName, isPlayerHelper);
            }
        }
        else {
            if(isPlayerOwner) { // Display Plot sell cancel
                return this.itemPalette.getPlotSellCancel(plotPrice);
            }
            else { // Display Plot buy
                
                return this.itemPalette.getPlotBuy(ownerName, plotPrice);
            }
        }
    }
    
    private ItemStack getHelperItem(UUID player) {
        
        return this.itemPalette.getHelper(
            this.helperCache.getHelperListFromPlayer(player)
        );
    }
    
    private void editHelperFromPlayer(Player player, String helperName) {
        
        final OfflinePlayer helper = Bukkit.getOfflinePlayer(helperName);
        
        if (helper == null || !helper.hasPlayedBefore()) {
            player.sendMessage(this.message.getHelperPlayerNoExists(helperName));
            return;
        }

        int result =
            this.helperCache.editHelperFromPlayer(
                player.getUniqueId(),
                helper.getUniqueId()
            );
    
        if(result == 1) { //delete
            player.sendMessage(
                this.message.getHelperRemove(helperName)
            );
        }
        else if(result == 2) { //insert
            
            player.sendMessage(
                this.message.getHelperAdd(helperName)
            );
            
            if(helper.isOnline())
                ((Player)helper).sendMessage(
                    this.message.getHelperInformHelper(player.getName())
                );
        }
    }
}
