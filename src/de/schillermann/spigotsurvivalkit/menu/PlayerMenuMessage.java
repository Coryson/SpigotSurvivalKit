package de.schillermann.spigotsurvivalkit.menu;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenuMessage {
    
    final private String helperAdd;
    
    final private String helperInformHelper;
    
    final private String helperName;
    
    final private String helperRemove;
    
    final private String helperPlayerNoExists;
    
    final private String menuTitle;
    
    final private String noBedSpawn;
    
    final private String plotSellRelease;
    
    final private String plotSellPriceInput;
    
    final private String plotSellPriceError;
    
    final private String plotSellCancelSuccess;
    
    final private String plotSellCancelError;
    
    final private String plotBuySuccess;
    
    final private String plotBuyBroke;
    
    public PlayerMenuMessage(FileConfiguration config) {
        
        this.menuTitle = config.getString("playermenu.menu_title");
        
        String rootPath = "playermenu.action.";
        this.noBedSpawn = config.getString(rootPath + "no_bed_spawn");
        
        String sellPath = rootPath + "plot.sell.";
        this.plotSellRelease = config.getString(sellPath + "release");
        this.plotSellPriceInput = config.getString(sellPath + "price.input");
        this.plotSellPriceError = config.getString(sellPath + "price.error");
        
        this.plotSellCancelSuccess = config.getString(rootPath + "plot.sell_cancel.success");
        this.plotSellCancelError = config.getString(rootPath + "plot.sell_cancel.error");
        
        this.plotBuySuccess = config.getString(rootPath + "plot.buy.success");
        this.plotBuyBroke = config.getString(rootPath + "plot.buy.broke");
        
        String helperPath = rootPath + "helper."; 
        this.helperAdd = config.getString(helperPath + "add");
        this.helperInformHelper = config.getString(helperPath + "inform_helper");
        this.helperName = config.getString(helperPath + "name");
        this.helperRemove = config.getString(helperPath + "remove");
        this.helperPlayerNoExists = config.getString(helperPath + "player_no_exists");
    }
    
    public String getHelperAdd(String name) {
        return String.format(this.helperAdd, name);
    }
    
    public String getHelperInformHelper(String name) {
        return String.format(this.helperInformHelper, name);
    }
    
    public String getHelperName() {
        return this.helperName;
    }
    
    public String getHelperRemove(String name) {
        return String.format(this.helperRemove, name);
    }
    
    public String getHelperPlayerNoExists(String name) {
        return String.format(this.helperPlayerNoExists, name);
    }
    
    public String getPlotSellRelease(int price) {
        return String.format(this.plotSellRelease, price);
    }
    
    public String getPlotSellPriceInput() {
        return this.plotSellPriceInput;
    }
    
    public String getPlotSellPriceError() {
        return this.plotSellPriceError;
    }
    
    public String getPlotSellCancelSuccess() {
        return this.plotSellCancelSuccess;
    }
    
    public String getPlotSellCancelError() {
        return this.plotSellCancelError;
    }
    
    public String getPlotBuySuccess() {
        return this.plotBuySuccess;
    }
    
    public String getPlotBuyBroke() {
        return this.plotBuyBroke;
    }
    
    public String getNoBedSpawn() {
        return this.noBedSpawn;
    }
    
    public String getMenuTitle() {
        return this.menuTitle;
    }
}
