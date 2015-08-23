package de.schillermann.spigotsurvivalkit.menu;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenuItemPalette {
    
    private ItemStack plotBuy;
    private ItemStack plotSell;
    private ItemStack plotSellCancel;
    private ItemStack plotOwner;
    private ItemStack helper;
    private ItemStack teleportToSpawn;
    private ItemStack teleportToBed;
    
    private String plotBuyOwner;
    private String plotBuyPrice;
    private String plotSellCancelDescription;
    
    private String plotOwnerTitle;
    private List<String> plotOwnerDescription;
    private List<String> plotOwnerIsPlayerHelper;
    private List<String> helperDescription;
    
    public PlayerMenuItemPalette(FileConfiguration config) {
        
        this.setPlotBuy(config);
        this.setPlotSell(config);
        this.setPlotOwner(config);
        this.setPlotSellCancel(config);
        this.setHelper(config);
        this.setTeleportToSpawn(config);
        this.setTeleportToBed(config);
    }
    
    public ItemStack getPlotBuy(String seller, int price) {
        
        List<String> description = new ArrayList<>();
        description.add(String.format(this.plotBuyOwner, seller));
        description.add(String.format(this.plotBuyPrice, price));
        
        ItemMeta plotBuyMeta = this.plotBuy.getItemMeta();
        plotBuyMeta.setLore(description);
        this.plotBuy.setItemMeta(plotBuyMeta);
       
        return this.plotBuy;
    }
    
    public ItemStack getPlotSell() {
        return this.plotSell;
    }
    
    public ItemStack getPlotOwner(String owner, boolean isPlayerHelper) {
        
        ItemMeta meta = this.plotOwner.getItemMeta();
        meta.setDisplayName(
            String.format(this.plotOwnerTitle, owner)
        );
        if(isPlayerHelper)
            meta.setLore(this.plotOwnerDescription);
        else
            meta.setLore(this.plotOwnerIsPlayerHelper);
        this.plotOwner.setItemMeta(meta);
        
        return this.plotOwner;
    }
    
    public ItemStack getPlotSellCancel(int price) {
        
        List<String> description = new ArrayList<>();
        description.add(
            String.format(this.plotSellCancelDescription, price)
        );
        
        ItemMeta meta = this.plotSellCancel.getItemMeta();
        meta.setLore(description);
        this.plotSellCancel.setItemMeta(meta);
        
        return this.plotSellCancel;
    }
    
    public ItemStack getHelper(List<String> helpers) {
        
        ItemMeta meta = this.helper.getItemMeta();
        
        if(helpers.isEmpty())
            meta.setLore(this.helperDescription);
        else
            meta.setLore(helpers);
        
        this.helper.setItemMeta(meta);
        
        return this.helper;
    }
    
    public ItemStack getTeleportToSpawn() {
        return this.teleportToSpawn;
    }
    
    public ItemStack getTeleportToBed() {
        return this.teleportToBed;
    }
    
    public boolean isPlotBuy(Material type) {
        return this.plotBuy.getType() == type;
    }
    
    public boolean isPlotSell(Material type) {
        return this.plotSell.getType() == type;
    }
    
    public boolean isPlotSellCancel(Material type) {
        return this.plotSellCancel.getType() == type;
    }
    
    public boolean isHelper(Material type) {
        return this.helper.getType() == type;
    }
    
    public boolean isTeleportToSpawn(Material type) {
        return this.teleportToSpawn.getType() == type;
    }
    
    public boolean isTeleportToBed(Material type) {
        return this.teleportToBed.getType() == type;
    }
    
    private void setPlotBuy(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.plot_buy.material")
            );
        String title =
            config.getString("playermenu.items.plot_buy.title");
        
        this.plotBuy = new ItemStack(material);
        ItemMeta meta = this.plotBuy.getItemMeta();
        meta.setDisplayName(title);
        this.plotBuy.setItemMeta(meta);
        
        this.plotBuyOwner = config.getString("playermenu.items.plot_buy.owner");
        this.plotBuyPrice = config.getString("playermenu.items.plot_buy.price");
    }
    
    private void setPlotSell(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.plot_sell.material")
            );
        String title =
            config.getString("playermenu.items.plot_sell.title");

        this.plotSell = new ItemStack(material);
        ItemMeta meta = this.plotSell.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(config.getStringList("playermenu.items.plot_sell.description"));
        this.plotSell.setItemMeta(meta);
    }
        
    private void setPlotOwner(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.plot_owner.material")
            );
        
        this.plotOwner = new ItemStack(material);
        this.plotOwnerTitle =
            config.getString("playermenu.items.plot_owner.title");
        this.plotOwnerDescription =
            config.getStringList("playermenu.items.plot_owner.description");
        this.plotOwnerIsPlayerHelper =
            config.getStringList("playermenu.items.plot_owner.is_helper");
    }
    
    private void setPlotSellCancel(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.plot_sell_cancel.material")
            );
        
        this.plotSellCancel = new ItemStack(material);
        ItemMeta meta = this.plotSellCancel.getItemMeta();
        meta.setDisplayName(config.getString("playermenu.items.plot_sell_cancel.title"));
        this.plotSellCancel.setItemMeta(meta);
        
        this.plotSellCancelDescription =
            config.getString("playermenu.items.plot_sell_cancel.description");
    }
    
    private void setHelper(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.helper.material")
            );
        
        String title =
            config.getString("playermenu.items.helper.title");
        
        this.helper = new ItemStack(material);
        ItemMeta meta = this.helper.getItemMeta();
        meta.setDisplayName(title);
        this.helper.setItemMeta(meta);
        
        this.helperDescription =
            config.getStringList("playermenu.items.helper.description");
    }
    
    private void setTeleportToSpawn(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.teleport_to_spawn.material")
            );
        
        String title =
            config.getString("playermenu.items.teleport_to_spawn.title");
        
        List<String> description =
            config.getStringList("playermenu.items.teleport_to_spawn.description");
        
        this.teleportToSpawn = new ItemStack(material);
        ItemMeta meta = this.teleportToSpawn.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(description);
        this.teleportToSpawn.setItemMeta(meta);
    }
    
    private void setTeleportToBed(FileConfiguration config) {
        
        Material material =
            Material.getMaterial(
                config.getString("playermenu.items.teleport_to_bed.material")
            );
        
        String title =
            config.getString("playermenu.items.teleport_to_bed.title");
        
        List<String> description =
            config.getStringList("playermenu.items.teleport_to_bed.description");
        
        this.teleportToBed = new ItemStack(material);
        ItemMeta meta = this.teleportToBed.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(description);
        this.teleportToBed.setItemMeta(meta);
    }
}