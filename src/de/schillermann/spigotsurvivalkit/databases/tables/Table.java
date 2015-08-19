package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario Schillermann
 */
public abstract class Table {

    final Connection database;
    final String table;
    final Logger logger;
    
    public Table(Connection database, String table, Logger logger) {
        this.database = database;
        this.table = table;
        this.logger = logger;
    }
    
    public String getTableName() {
        return this.table;
    }
    
    protected boolean createTable(String sql) {
        
        try {
            Statement stmt = this.database.createStatement();
            return stmt.executeUpdate(sql) == 0;
            
        } catch (SQLException e) {
            this.logger.log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }
    }
}
