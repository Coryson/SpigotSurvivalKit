package de.schillermann.spigotsurvivalkit.entities;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Mario Schillermann
 */
final public class PlayerMenu {
    
    public static Inventory getInventory(Player player) {
        
        ItemStack[] menuPoints = new ItemStack[6];
        
        ArrayList<String> plotBuyMetaList = new ArrayList<>();
        plotBuyMetaList.add("Preis: 1 Diamand");
        
        menuPoints[0] = new ItemStack(Material.GRASS);
        ItemMeta plotBuyMeta = menuPoints[0].getItemMeta();
        plotBuyMeta.setDisplayName("Grundstück kaufen");
        plotBuyMeta.setLore(plotBuyMetaList);
        menuPoints[0].setItemMeta(plotBuyMeta);
        
        ArrayList<String> plotSellMetaList = new ArrayList<>();
        plotSellMetaList.add("1 Slot im Inventar");
        plotSellMetaList.add("muss dafür frei sein");
        
        menuPoints[1] = new ItemStack(Material.DIAMOND);
        ItemMeta plotSellMeta = menuPoints[1].getItemMeta();
        plotSellMeta.setDisplayName("Grundstück verkaufen");
        plotSellMeta.setLore(plotSellMetaList);
        menuPoints[1].setItemMeta(plotSellMeta);
        
        ArrayList<String> helperMetaList = new ArrayList<>();
        helperMetaList.add("Hans");
        helperMetaList.add("Maler");
        helperMetaList.add("Günter");
        
        menuPoints[2] = new ItemStack(Material.SKULL_ITEM);
        ItemMeta helperMeta = menuPoints[2].getItemMeta();
        helperMeta.setDisplayName("Bauhelfer Liste");
        helperMeta.setLore(helperMetaList);
        menuPoints[2].setItemMeta(helperMeta);
        
        ArrayList<String> teleportToSpawnMetaList = new ArrayList<>();
        teleportToSpawnMetaList.add("Bringt dich zum Spawn");
        
        menuPoints[3] = new ItemStack(Material.BEACON);
        ItemMeta teleportToSpawnMeta = menuPoints[3].getItemMeta();
        teleportToSpawnMeta.setDisplayName("Spawn Teleport");
        teleportToSpawnMeta.setLore(teleportToSpawnMetaList);
        menuPoints[3].setItemMeta(teleportToSpawnMeta);
        
        ArrayList<String> teleportToHomeMetaList = new ArrayList<>();
        teleportToHomeMetaList.add("Bringt dich nach Hause,");
        teleportToHomeMetaList.add("wenn du dein Zuhause");
        teleportToHomeMetaList.add("gesetzt hast");
        
        menuPoints[4] = new ItemStack(Material.ENDER_PEARL);
        ItemMeta teleportToHomeMeta = menuPoints[4].getItemMeta();
        teleportToHomeMeta.setDisplayName("Nach Hause Teleport");
        teleportToHomeMeta.setLore(teleportToHomeMetaList);
        menuPoints[4].setItemMeta(teleportToHomeMeta);
        
        ArrayList<String> setHomeMetaList = new ArrayList<>();
        setHomeMetaList.add("Setze dein Zuhause");
        setHomeMetaList.add("um dich dort hin");
        setHomeMetaList.add("bringen zu lassen");
        
        menuPoints[5] = new ItemStack(Material.ENDER_CHEST);
        ItemMeta setHomeMeta = menuPoints[5].getItemMeta();
        setHomeMeta.setDisplayName("Zuhause setzen");
        setHomeMeta.setLore(setHomeMetaList);
        menuPoints[5].setItemMeta(setHomeMeta);
        
        Inventory menu = Bukkit.createInventory(player, 9, "Menu");
        menu.addItem(menuPoints);
        
        return menu;
    }
}
