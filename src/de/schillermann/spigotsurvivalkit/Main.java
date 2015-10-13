package de.schillermann.spigotsurvivalkit;

import de.schillermann.spigotsurvivalkit.cache.*;
import de.schillermann.spigotsurvivalkit.commands.*;
import de.schillermann.spigotsurvivalkit.listeners.*;
import de.schillermann.spigotsurvivalkit.menu.*;
import de.schillermann.spigotsurvivalkit.services.*;
import de.schillermann.spigotsurvivalkit.databases.DatabaseProvider;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpLocation;
import de.schillermann.spigotsurvivalkit.utils.ChunkRegeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Mario Schillermann
 */
final public class Main extends JavaPlugin {

    private DatabaseProvider database;
    
    private HelperCache cacheHelper;
    private PlotCache cachePlot;
    private ChunkLogCache cacheChunkLog;
    private BankProvider providerBank;
    private PlotProvider providerPlot;
    
    @Override
    public void onEnable(){
        
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
         
        this.database = new DatabaseProvider();
        
        if(!this.database.isInitialize()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
                
        this.onCache(this.getConfig());
        
        this.providerBank = new BankProvider(
            this.database.getTableBank(),
            Material.getMaterial(this.getConfig().getString("currency"))
        );
        
        this.providerPlot = new PlotProvider(
            this.database.getTablePlot(),
            this.cachePlot,
            this.database.getTableChunkLog(),
            providerBank,
            this.getConfig().getInt("plot.price_default")
        );
        
        this.onCommand(this.getConfig());
        
        this.onListener(
            this.getConfig(),
            this.getServer().getPluginManager()
        );
        
        Bukkit.getScheduler().runTask(
            this,
            new ChunkRegeneration(
                this.database.getTableChunkLog(),
                this.getConfig().getString("chunks.regenerate.success"),
                this.getConfig().getString("chunks.regenerate.error")
            )
        );
    }
    
    @Override
    public void onDisable() {
        
        this.database.close();
    }
    
    private void onCache(FileConfiguration config) {

        this.cacheChunkLog = new ChunkLogCache(
            this.database.getTableChunkLog(),
            config.getInt("cachesize.chunklog")
        );
        
        this.cacheHelper = new HelperCache(
            this.database.getTableHelper(),
            config.getInt("cachesize.helper")
        );
        
        this.cachePlot = new PlotCache(
            this.database.getTablePlot(),
            config.getInt("cachesize.plotprotect")
        );
    }
    
    private void onCommand(FileConfiguration config) {

        BankCommand bankCommand = new BankCommand(
            this.providerBank,
            new BankCommandMessage(config)
        );
        this.getCommand("bank").setExecutor(bankCommand);
        
        PlotCommand plotCommand = new PlotCommand(
            this.providerPlot,
            new PlotCommandMessage(config)
        );
        this.getCommand("plot").setExecutor(plotCommand);
    }
    
    private void onListener(
        FileConfiguration config,
        PluginManager pm
    ) {
        
        ChunkListener chunk = new ChunkListener(
            this.cachePlot,
            this.cacheChunkLog,
            this.cacheHelper,
            new PlotMessage(config)
        );
        pm.registerEvents(chunk, this);
        
        LockListener lock = new LockListener(
            this.database.getTableLock(),
            new LockMessage(config)
        );
        pm.registerEvents(lock, this);
        
        MainMenu menuMain = new MainMenu(
            this,
            new MainMenuItems(config),
            new MainMenuMessage(config),
            this.providerPlot,
            this.cacheHelper
        );
        
        WarpsMenu menuWarps = new WarpsMenu(
            config.getString("warpsmenu.menu_title"),
            this.database.getTableWarp()    
        );
        
        RemoveWarpCommand removeWarpCommand = new RemoveWarpCommand(
            this.database.getTableWarp(),
            new RemoveWarpCommandMessage(config),
            menuWarps
        );
        
        this.getCommand("removewarp").setExecutor(removeWarpCommand);
        
        SetWarpCommand setWarpCommand = new SetWarpCommand(
            this.database.getTableWarp(),
            new SetWarpCommandMessage(config),
            menuWarps
        );
        
        this.getCommand("setwarp").setExecutor(setWarpCommand);
        
        PlayerMenuListener menu = new PlayerMenuListener(
            this,
            Material.getMaterial(config.getString("mainmenu.open_with_item")),
            menuMain,
            menuWarps
        );
        
        pm.registerEvents(menu, this);
        
        String warpsFirstJoinName = config.getString("warps.firstjoin");
        String warpsRespawnName = config.getString("warps.respawn");
        
        WarpLocation locationFirstJoin =
            this.database.getTableWarp().selectWarp(warpsFirstJoinName);
        
        WarpLocation locationRespawn =
            this.database.getTableWarp().selectWarp(warpsRespawnName);
        
        if(locationFirstJoin == null) {
            
            Bukkit.getLogger().info(
                String.format(
                    "[%s] The warp %s is not set for the first player join",
                    this.getName(),
                    warpsFirstJoinName
                )
            );
        }
        
        if(locationRespawn == null) {
            
            Bukkit.getLogger().info(
                String.format(
                    "[%s] The warp %s is not set for respawn after player death",
                    this.getName(),
                    warpsRespawnName
                )
            );
        }

        DefaultWarps defaultWarps =
            new DefaultWarps(locationFirstJoin, locationRespawn);
        
        PlayerListener player = new PlayerListener(
            this.providerBank,
            new Stats(this.database.getTableStats(), new StatsConfig(config)),
            new JoinMessage(config),
            defaultWarps
        );
        pm.registerEvents(player, this);
        
        boolean enabledVotifier =
            this.getServer().getPluginManager().isPluginEnabled("Votifier");
        
        if(enabledVotifier) {
            VoteListener vote = new VoteListener(
                this.providerBank,
                config.getInt("vote.price"),
                config.getString("vote.thanks")
            );

            pm.registerEvents(vote, this);
        }
        else {
            String infoMsg =
                "[%s] Plugin Votifier is missing for the vote function";
            
            Bukkit.getLogger().info(String.format(infoMsg, this.getName()));
        }
    }
}
