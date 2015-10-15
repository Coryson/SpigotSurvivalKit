package de.schillermann.spigotsurvivalkit.menu.main;

import de.schillermann.spigotsurvivalkit.menu.item.HelpersMenuItem;
import de.schillermann.spigotsurvivalkit.menu.item.MenuItem;
import de.schillermann.spigotsurvivalkit.menu.item.PlotBuyMenuItem;
import de.schillermann.spigotsurvivalkit.menu.item.PlotOwnerMenuItem;
import de.schillermann.spigotsurvivalkit.menu.item.PlotSellCancelMenuItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
public class MainMenuItems {
    
    final private PlotBuyMenuItem itemPlotBuy;
    
    final private MenuItem itemPlotSell;
    
    final private PlotSellCancelMenuItem itemPlotSellCancel;
    
    final private PlotOwnerMenuItem itemPlotOwner;
    
    final private HelpersMenuItem itemHelpers;
    
    final private MenuItem itemVoteLinks;
    
    final private MenuItem itemWarps;
    
    public MainMenuItems(FileConfiguration config) {
        
        String rootPath = "mainmenu.items.";
        
        this.itemPlotBuy = new PlotBuyMenuItem(
            Material.getMaterial(
                config.getString(rootPath + "plot_buy.material")
            ),
            config.getString(rootPath + "plot_buy.name"),
            config.getStringList(rootPath + "plot_buy.description")
        );
                
        this.itemPlotSell = new MenuItem(
            Material.getMaterial(
                config.getString(rootPath + "plot_sell.material")
            ),
            config.getString(rootPath + "plot_sell.name"),
            config.getStringList(rootPath + "plot_sell.description")
        );
        
        this.itemPlotSellCancel = new PlotSellCancelMenuItem(
            Material.getMaterial(
                config.getString(rootPath + "plot_sell_cancel.material")
            ),
            config.getString(rootPath + "plot_sell_cancel.name"),
            config.getStringList(rootPath + "plot_sell_cancel.description")
        );
        
        this.itemPlotOwner = new PlotOwnerMenuItem(
            Material.getMaterial(
                config.getString(rootPath + "plot_owner.material")
            ),
            config.getString(rootPath + "plot_owner.name"),
            config.getStringList(rootPath + "plot_owner.description"),
            config.getStringList(rootPath + "plot_owner.is_helper")
        );
        
        this.itemHelpers = new HelpersMenuItem(
            Material.getMaterial(
                config.getString(rootPath + "helpers.material")
            ),
            config.getString(rootPath + "helpers.name"),
            config.getStringList(rootPath + "helpers.description")
        );
        
        this.itemWarps = new MenuItem(
            Material.getMaterial(
                config.getString(rootPath + "warps.material")
            ),
            config.getString(rootPath + "warps.name"),
            config.getStringList(rootPath + "warps.description")
        );
        
        this.itemVoteLinks = new MenuItem(
            Material.getMaterial(
                config.getString(rootPath + "votelinks.material")
            ),
            config.getString(rootPath + "votelinks.name"),
            config.getStringList(rootPath + "votelinks.description")
        );
    }
    
    public PlotBuyMenuItem getItemPlotBuy() {
        return this.itemPlotBuy;
    }
    
    public MenuItem getItemPlotSell() {
        return this.itemPlotSell;
    }
            
    public PlotSellCancelMenuItem getItemPlotSellCancel() {
        return this.itemPlotSellCancel;
    }
    
    public PlotOwnerMenuItem getItemPlotOwner() {
        return this.itemPlotOwner;
    }
    
    public HelpersMenuItem getItemHelpers() {
        return this.itemHelpers;
    }
    
    public MenuItem getItemWarps() {
        return this.itemWarps;
    }
    
    public MenuItem getItemVoteLinks() {
        return this.itemVoteLinks;
    }
}
