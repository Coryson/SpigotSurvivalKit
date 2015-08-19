package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkProtectTable extends Table {

    public ChunkProtectTable(Connection database, Logger logger) {
        super(database, "chunk_protect", logger);
    }
    
    public UUID selectPlayerUuidFromChunkProtect(Chunk chunk) {
        
        String sql =
            "SELECT player_uuid FROM " + this.table + " WHERE x = ? AND z = ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, chunk.getX());
            stmt.setInt(2, chunk.getZ());
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
                return UUID.fromString(rs.getString("player_uuid"));
            else
                return null;
        }
        catch (SQLException e) {
            
            this.logger.log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return null;
        }
    }
    
    public boolean insertChunkProtect(int x, int z, UUID player) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (x, z, player_uuid, created)" +
            " VALUES (?, ?, ?, strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, x);
            stmt.setInt(2, z);
            stmt.setString(3, player.toString());
            
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            this.logger.log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return false;
        }
    }
    
    public boolean deleteChunkProtect(int x, int z) {
        
        String sql = "DELETE FROM " + this.table + " WHERE x = ? AND z = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, x);
            stmt.setInt(2, z);
            
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            this.logger.log(
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
            "x INTEGER NOT NULL," +
            "z INTEGER NOT NULL," +
            "player_uuid TEXT NOT NULL," +
            "created INTEGER NOT NULL," +
            "PRIMARY KEY(x,z)" +
            ");"
        );
    }
}
