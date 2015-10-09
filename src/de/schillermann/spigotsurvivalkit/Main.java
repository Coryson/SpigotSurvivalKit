package de.schillermann.spigotsurvivalkit;

import de.schillermann.spigotsurvivalkit.services.PlotProvider;
import de.schillermann.spigotsurvivalkit.cache.*;
import de.schillermann.spigotsurvivalkit.commands.BankCommand;
import de.schillermann.spigotsurvivalkit.commands.BankCommandMessage;
import de.schillermann.spigotsurvivalkit.commands.PlotCommand;
import de.schillermann.spigotsurvivalkit.commands.PlotCommandMessage;
import de.schillermann.spigotsurvivalkit.listeners.*;
import de.schillermann.spigotsurvivalkit.menu.*;
import de.schillermann.spigotsurvivalkit.commands.PrisonCommand;
import de.schillermann.spigotsurvivalkit.databases.DatabaseProvider;
import de.schillermann.spigotsurvivalkit.menu.PlayerMenuListener;
import de.schillermann.spigotsurvivalkit.menu.PlayerMenu;
import de.schillermann.spigotsurvivalkit.listeners.PlotMessage;
import de.schillermann.spigotsurvivalkit.services.BankProvider;
import de.schillermann.spigotsurvivalkit.services.Stats;
import de.schillermann.spigotsurvivalkit.services.StatsConfig;
import de.schillermann.spigotsurvivalkit.utils.ChunkRegeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        saveConfig();
        
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
            this.getServer().getPluginManager(),
            this.getConfig()
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
        
        Location prisonLocation = new Location(
            Bukkit.getWorld(config.getString("prisonspawn.world")),
            config.getInt("prisonspawn.x"),
            config.getInt("prisonspawn.y"),
            config.getInt("prisonspawn.z")
        );
        
        PrisonCommand prisonCommand = new PrisonCommand(
            prisonLocation,
            config.getString("prisonspawn.broadcast"),
            config.getString("player_not_found")
        );
        
        this.getCommand("jail").setExecutor(prisonCommand);
        
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
        PluginManager pm,
        FileConfiguration config
    ) {
        
        ChunkListener chunk = new ChunkListener(
            this.cachePlot,
            this.cacheChunkLog,
            this.cacheHelper,
            new PlotMessage(config)
        );
        pm.registerEvents(chunk, this);
        
        Location deathLocation = new Location(
            Bukkit.getWorld(config.getString("hospital.world")),
            config.getInt("hospital.x"),
            config.getInt("hospital.y"),
            config.getInt("hospital.z")
        );
        
        Hospital hospital = new Hospital(
            deathLocation,
            config.getString("hospital.info")
        );
        
        PlayerListener player = new PlayerListener(
            this.providerBank,
            new Stats(this.database.getTableStats(), new StatsConfig(config)),
            hospital,
            new JoinMessage(config)
        );
        pm.registerEvents(player, this);
        
        LockListener lock = new LockListener(
            this.database.getTableLock(),
            new LockMessage(config)
        );
        pm.registerEvents(lock, this);
        
        PlayerMenu playerMenu = new PlayerMenu(
            this,
            new PlayerMenuItemPalette(config),
            new PlayerMenuMessage(config),
            this.providerPlot,
            this.cacheHelper
        );
        
        PlayerMenuListener menu = new PlayerMenuListener(
            Material.getMaterial(config.getString("playermenu.open_with_item")),
            playerMenu
        );
        
        pm.registerEvents(menu, this);
        
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
