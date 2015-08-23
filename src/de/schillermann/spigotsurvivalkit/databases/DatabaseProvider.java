package de.schillermann.spigotsurvivalkit.databases;

import de.schillermann.spigotsurvivalkit.databases.tables.BankTable;
import de.schillermann.spigotsurvivalkit.databases.tables.ChunkLogTable;
import de.schillermann.spigotsurvivalkit.databases.tables.HelperTable;
import de.schillermann.spigotsurvivalkit.databases.tables.LockTable;
import de.schillermann.spigotsurvivalkit.databases.tables.PlotTable;
import de.schillermann.spigotsurvivalkit.databases.tables.StatsTable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Mario Schillermann
 */
final public class DatabaseProvider {

    private Connection connection = null;
    
    private PlotTable tablePlot;
    
    private ChunkLogTable tableChunkLog;
    
    private HelperTable tableHelper;
    
    private LockTable tableLock;
    
    private StatsTable tableStats;
    
    private BankTable tableBank;
    
    public boolean isInitialize() {
    
        File bukkitFile = new File("bukkit.yml");
        YamlConfiguration bukkitConfig =
            YamlConfiguration.loadConfiguration(bukkitFile);
        
        String driver = bukkitConfig.getString("database.driver");
        String url = bukkitConfig.getString("database.url");
        
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url);

        } catch ( ClassNotFoundException | SQLException e ) {

            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }

        this.tablePlot = new PlotTable(this.connection);
        this.tableHelper = new HelperTable(this.connection);
        this.tableChunkLog = new ChunkLogTable(this.connection);
        this.tableLock = new LockTable(this.connection);
        this.tableStats = new StatsTable(this.connection);
        this.tableBank = new BankTable(this.connection);

        return !(!this.tablePlot.createTable() ||
                !this.tableHelper.createTable() ||
                !this.tableChunkLog.createTable() ||
                !this.tableLock.createTable() ||
                !this.tableStats.createTable() ||
                !this.tableBank.createTable());
    }
    
    public void close() {
        
        if(this.connection != null) {
            
            try {
                this.connection.close();
            } catch (SQLException e) {
                Bukkit.getLogger().log(
                    Level.WARNING,
                    "{0}: {1}",
                    new Object[]{e.getClass().getName(), e.getMessage()}
                );
            }
        }
    }
    
    public PlotTable getTablePlot() {
        return this.tablePlot;
    }
    
    public HelperTable getTableHelper() {
        return this.tableHelper;
    }
    
    public ChunkLogTable getTableChunkLog() {
        return this.tableChunkLog;
    }
    
    public LockTable getTableLock() {
        return this.tableLock;
    }
    
    public StatsTable getTableStats() {
        return this.tableStats;
    }
    
    public BankTable getTableBank() {
        return this.tableBank;
    }
}
