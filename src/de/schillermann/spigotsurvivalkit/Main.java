package de.schillermann.spigotsurvivalkit;

import de.schillermann.spigotsurvivalkit.cache.ChunkProtectCache;
import de.schillermann.spigotsurvivalkit.cache.HelperCache;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkProtectTable;
import de.schillermann.spigotsurvivalkit.listeners.MenuListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Mario Schillermann
 */
final public class Main extends JavaPlugin {

    Connection database = null;
    HelperCache cacheHelper;
    ChunkProtectCache cacheChunkProtect;
    ChunkProtectTable tableChunkProtect;
    
    @Override
    public void onEnable(){
        
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        
        if(!this.onDatabase()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.onCache(this.getConfig());
        this.onCommand(this.getConfig());
        this.onListener(this.getServer().getPluginManager(), this.getConfig());
    }
    
    @Override
    public void onDisable() {
        
        if(database != null) {
            
            try {
                database.close();
            } catch (SQLException e) {
                this.getLogger().log(
                    Level.WARNING,
                    "{0}: {1}",
                    new Object[]{e.getClass().getName(), e.getMessage()}
                );
            }
        }
    }
    
    boolean onDatabase() {
        try {
          Class.forName("org.sqlite.JDBC");
          this.database = DriverManager.getConnection("jdbc:sqlite:knuddelcraft.db");
          
        } catch ( ClassNotFoundException | SQLException e ) {
          
            this.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }
        
        this.tableChunkProtect = new ChunkProtectTable(
            this.database, this.getLogger()
        );
        
        if(this.tableChunkProtect.createTable())
            this.getLogger().log(
                Level.INFO,
                "Table {0} was created.",
                this.tableChunkProtect.getTableName()
            );
        
        return true;
    }

    void onCache(FileConfiguration config) {
        /*
        this.cacheHelper = new HelperCache(
            this.helperTable,
            config.getInt("cachesize.helper")
        );*/
        
        this.cacheChunkProtect = new ChunkProtectCache(
            this.tableChunkProtect,
            config.getInt("cachesize.chunkprotect")
        );
    }
    
    void onCommand(FileConfiguration config) {
        
        /*
        BuyCommand buyCommand = new BuyCommand(
            this.tableChunkProtect,
            this.cacheChunkProtect,
            Material.DIAMOND,
            config.getInt("chunk.price"),
            config.getString("message.chunk.bought.success"),
            config.getString("message.chunk.bought.broke"),
            config.getString("message.chunk.bought.failure"),
            config.getString("message.chunk.bought.forbidden")
        );
	this.getCommand("kaufen").setExecutor(buyCommand);
        
        SellCommand sellCommand = new SellCommand(
            this.chunkProtectTable,
            this.chunkProtectCache,
            this.shopApi,
            config.getString("message.chunk.sold.success"),
            config.getString("message.chunk.sold.failure"),
            config.getString("message.chunk.sold.full_inventory"),
            config.getInt("chunk.price")
        );
	this.getCommand("verkaufen").setExecutor(sellCommand);
        
        HelperCommand helperCommand = new HelperCommand(
            this.helperTable,
            this.helperCache,
            config.getString("message.chunk.helper.add"),
            config.getString("message.chunk.helper.remove"),
            config.getString("message.chunk.helper.list"),
            config.getString("message.chunk.helper.other"),
            config.getString("message.player.online")
        );
        
	this.getCommand("bauhelfer").setExecutor(helperCommand);
        
        ChunkCommand chunkCommand = new ChunkCommand(
            this.chunkTable,
            this.chunkProtectTable,
            config.getString("message.chunk.regenerate.chunk.error"),
            config.getString("message.chunk.regenerate.chunk.success"),
            config.getString("message.chunk.regenerate.chunk.bought"),    
            config.getString("message.chunk.regenerate.chunks.success"),
            config.getString("message.chunk.regenerate.chunks.error")
        );
        this.getCommand("chunk").setExecutor(chunkCommand);*/
    }
    
    void onListener(PluginManager pm, FileConfiguration config) {
        
        MenuListener menu = new MenuListener();
        pm.registerEvents(menu, this);
    }
}
