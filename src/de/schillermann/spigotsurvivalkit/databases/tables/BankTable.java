package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 *
 * @author Mario Schillermann
 */
final public class BankTable extends Table {
    
    public BankTable(Connection database) {
        super(database, "bank");
    }
    
    public boolean insertBalance(UUID player, int balance) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (player_uuid, balance, updated, created) " +
            "VALUES (?, ?, strftime('%s','now'), strftime('%s','now'))";
  
        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, player.toString());
            stmt.setInt(2, balance);
            stmt.executeUpdate();
            
            return true;
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }
    }
    
    public boolean updateBalance(UUID player, int balance) {
        
        String sql =
            "UPDATE " + this.table +
            " SET balance = ?, updated = strftime('%s','now')" +
            " WHERE player_uuid = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, balance);
            stmt.setString(2, player.toString());
            
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }
    }
    
    public int selectBalance(UUID player) {
        
        String sql =
            "SELECT balance FROM " + this.table + " WHERE player_uuid = ?";

        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, player.toString());
            ResultSet rs = stmt.executeQuery();
                  
            if(rs.next())
                return rs.getInt("balance");
            else
                return -1;
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return -2;
        }
    }
    
    public boolean createTable() {
 
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "player_uuid TEXT NOT NULL PRIMARY KEY," +
            "balance INTEGER NOT NULL," +
            "updated INTEGER NOT NULL," +
            "created INTEGER NOT NULL" +
            ");"
        );
    }
}
