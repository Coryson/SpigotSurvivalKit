package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 *
 * @author Mario Schillermann
 */
public abstract class Table {

    final Connection database;
    
    final String table;
    
    public Table(Connection database, String table) {
        this.database = database;
        this.table = table;
    }
    
    public String getTableName() {
        return this.table;
    }
    
    boolean createTable(String sql) {
        
        try {
            Statement stmt = this.database.createStatement();
            stmt.executeUpdate(sql);

            return true;
            
        } catch (SQLException e) {

            Bukkit.getLogger().log(
                Level.WARNING,
                "Create Table {0} is failed! {1}: {2}",
                new Object[]{
                    this.getTableName(),
                    e.getClass().getName(),
                    e.getMessage()
                }
            );
            return false;
        }
    }
}
