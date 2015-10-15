package de.schillermann.spigotsurvivalkit.menu.main;

import de.schillermann.spigotsurvivalkit.services.PlotProvider;
import de.schillermann.spigotsurvivalkit.cache.HelperCache;
import de.schillermann.spigotsurvivalkit.cache.HelperCache.HelperAction;
import de.schillermann.spigotsurvivalkit.menu.PlayerMenu;
import de.schillermann.spigotsurvivalkit.entities.PlotMetadata;
import de.schillermann.spigotsurvivalkit.menu.warps.WarpsMenuHolder;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Mario Schillermann
 */
final public class MainMenu implements PlayerMenu {
        
    final private MainMenuItems menuItems;
    
    final private MainMenuMessage message;
    
    final private PlotProvider providerPlot;
    
    final private HelperCache helperCache;
    
    final private String bankName = "Bank";
    
    /**
     * <player uuid hashcode, selected item name>
     */
    final private HashMap<Integer, String> needMetadata;
    
    public MainMenu(
        Plugin plugin,
        MainMenuItems menuItems,
        MainMenuMessage message,
        PlotProvider providerPlot,
        HelperCache helperCache
    ) {
        
        this.menuItems = menuItems;
        this.message = message;
        this.providerPlot = providerPlot;
        this.helperCache = helperCache;
        this.needMetadata = new HashMap<>();
    }
    
    @Override
    public Inventory getInventory(Player player) {
        
        final UUID playerUuid = player.getUniqueId();
        
        ItemStack[] menuContent = new ItemStack[] {
            this.getPlotItem(player.getLocation().getChunk(), playerUuid),
            this.getHelperItem(playerUuid),
            this.menuItems.getItemWarps().getItem(),
            this.menuItems.getItemVoteLinks().getItem()
        };
        
        Inventory menu = Bukkit.createInventory(
            new MainMenuHolder(),
            9,
            this.message.getMenuTitle()
        );
        menu.addItem(menuContent);
        
        return menu;
    }
    
    @Override
    public InventoryHolder inventoryItemAction(Player player, String selectedItemName) {
        
        
        final UUID playerUuid = player.getUniqueId();
        final Integer playerUuidHash = playerUuid.hashCode();
        final Chunk chunk = player.getLocation().getChunk();
        final PlotMetadata metadata = this.providerPlot.getPlotMetadata(chunk);
        int plotPrice = this.providerPlot.getPlotPriceDefault();
        boolean isPlayerOwner = false;
        
        if(metadata != null) {
           
            isPlayerOwner = metadata.getOwner().equals(playerUuid);
            plotPrice = metadata.getPrice();
        }
        
        if(this.menuItems.getItemWarps().getName().equals(selectedItemName)) {
            
            return new WarpsMenuHolder();
        }
        
        if(this.menuItems.getItemHelpers().getName().equals(selectedItemName) && isPlayerOwner) {
            
            player.sendMessage(this.message.getHelperName());
            
            this.needMetadata.remove(playerUuidHash);
            this.needMetadata.put(playerUuidHash, selectedItemName);
        }
        else if(this.menuItems.getItemPlotBuy().getName().equals(selectedItemName) && plotPrice > 0) {
            
            if(this.providerPlot.buyPlot(player))
                player.sendMessage(this.message.getPlotBuySuccess());
            else
                player.sendMessage(this.message.getPlotBuyBroke());
        }
        else if(this.menuItems.getItemPlotSellCancel().getName().equals(selectedItemName) && isPlayerOwner) {
            
            if(this.providerPlot.sellPlotCancel(chunk))
                player.sendMessage(this.message.getPlotSellCancelSuccess());
        }
        else if(this.menuItems.getItemPlotSell().getName().equals(selectedItemName) && isPlayerOwner) {
            
            player.sendMessage(this.message.getPlotSellPriceInput());
            
            this.needMetadata.remove(playerUuidHash);
            this.needMetadata.put(playerUuidHash, selectedItemName);
        }
        else if(this.menuItems.getItemVoteLinks().getName().equals(selectedItemName)) {
            
            this.message.getVoteList().stream().forEach((voteLink) -> {
                player.sendMessage(voteLink);
            });
        }
        
        return null;
    }
    
    public boolean isMetadata(Player player, String chatMessage) { 
        
        Integer playerUuidHash = player.getUniqueId().hashCode();
        if(!this.needMetadata.containsKey(playerUuidHash)) return false;
 
        String selectedItemName = this.needMetadata.get(playerUuidHash);
        this.needMetadata.remove(playerUuidHash);
        
        if(this.menuItems.getItemHelpers().getName().equals(selectedItemName)) {
            this.editHelperFromPlayer(player, chatMessage);
            return true;
        }
        else if(this.menuItems.getItemPlotSell().getName().equals(selectedItemName)) {
            
            int price = 0;

            try {  
                price = Integer.parseInt(chatMessage);
            }
            catch(NumberFormatException e) {}  
            
            if(
                price > 0 &&
                this.providerPlot.sellPlot(player.getLocation().getChunk(), price)
            )

                player.sendMessage(this.message.getPlotSellRelease(price));
            else
                player.sendMessage(this.message.getPlotSellPriceError());
            
            return true;
        }
        
        return false;
    }
    
    private ItemStack getPlotItem(Chunk chunk, UUID player) {
        
        PlotMetadata metadata = this.providerPlot.getPlotMetadata(chunk);
        
        if(metadata == null) {
         
            return this.menuItems.getItemPlotBuy().getItem(
                this.bankName,
                this.providerPlot.getPlotPriceDefault()
            );
        }
        
        UUID owner = metadata.getOwner();
        int plotPrice = metadata.getPrice();
        OfflinePlayer offlineOwner = Bukkit.getOfflinePlayer(metadata.getOwner());
        String ownerName = offlineOwner.getName();
        
        boolean isPlayerOwner =
            owner.hashCode() == player.hashCode();
        
        if(plotPrice == 0) {
            if(isPlayerOwner) { // Display Plot sell
                return this.menuItems.getItemPlotSell().getItem();
            }
            else { // Display Plot Owner
                boolean isPlayerHelper =
                    this.helperCache.hasPlayerThisHelper(owner, player);
                
                return this.menuItems.getItemPlotOwner().getItem(
                    ownerName,
                    isPlayerHelper
                );
            }
        }
        else {
            if(isPlayerOwner) { // Display Plot sell cancel
                return this.menuItems.getItemPlotSellCancel().getItem(plotPrice);
            }
            else { // Display Plot buy
                
                return this.menuItems.getItemPlotBuy().getItem(
                    ownerName,
                    plotPrice
                );
            }
        }
    }
    
    private ItemStack getHelperItem(UUID player) {
        
        return this.menuItems.getItemHelpers().getItem(
            this.helperCache.getHelperListFromPlayer(player)
        );
    }
    
    private void editHelperFromPlayer(Player player, String helperName) {
        
        final OfflinePlayer helper = Bukkit.getOfflinePlayer(helperName);
        
        if (helper == null || !helper.hasPlayedBefore()) {
            player.sendMessage(this.message.getHelperPlayerNoExists(helperName));
            return;
        }

        HelperCache.HelperAction result =
            this.helperCache.editHelperFromPlayer(
                player.getUniqueId(),
                helper.getUniqueId()
            );
        
        if(result == HelperAction.DELETE) {
            player.sendMessage(
                this.message.getHelperRemove(helperName)
            );
        }
        else if(result == HelperAction.INSERT) {
            
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
