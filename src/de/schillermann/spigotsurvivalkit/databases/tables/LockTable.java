package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

/**
 *
 * @author Mario Schillermann
 */
final public class LockTable extends Table {

    public LockTable(Connection database) {
        super(database, "lock");
    }
 
    public boolean insertLock(UUID player, Block block) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (block_hash, player_uuid, created)" +
            " VALUES (?, ?, strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, block.hashCode());
            stmt.setString(2, player.toString());
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
    
    public UUID selectPlayerUuidFromLock(Block block) {
        
        String sql =
            "SELECT player_uuid FROM " + this.table + " WHERE block_hash = ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, block.hashCode());
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
                return UUID.fromString(rs.getString("player_uuid"));
            else
                return null;
        }
        catch (SQLException e) {
            
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return null;
        }
    }
    
    public boolean deleteLock(Block block) {
        
        String sql = "DELETE FROM " + this.table + " WHERE block_hash = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, block.hashCode());
            
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
    
    public boolean createTable() {
        
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "block_hash INTEGER NOT NULL PRIMARY KEY," +
            "player_uuid TEXT NOT NULL," +
            "created INTEGER NOT NULL" +
            ");"
        );
    }
}
