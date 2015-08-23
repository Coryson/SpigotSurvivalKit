package de.schillermann.spigotsurvivalkit.databases.tables;

import de.schillermann.spigotsurvivalkit.databases.tables.entities.ChunkLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class ChunkLogTable extends Table {
    
    public ChunkLogTable(Connection database) {
        super(database, "chunk_log");
    }
    
    public boolean insertChunk(int x, int z) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table + " (x, z) VALUES (?, ?)";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, x);
            stmt.setInt(2, z);
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
    
    public boolean deleteChunk(Chunk chunk) {
        
        String sql =
            "DELETE FROM " + this.table + " WHERE x = ? AND z = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, chunk.getX());
            stmt.setInt(2, chunk.getZ());
            
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
    
    public List<ChunkLocation> selectChunks() {
        
        String sql =
            "SELECT x, z FROM " + this.table;
        
        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<ChunkLocation> chunkList = new ArrayList<>();
            
            while(rs.next())
                chunkList.add(
                    new ChunkLocation(rs.getInt("x"), rs.getInt("z"))
                );
            
            return chunkList;
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
    
    public boolean createTable() {
 
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "x INTEGER NOT NULL," +
            "z INTEGER NOT NULL," +
            "PRIMARY KEY(x,z)" +
            ");"
        );
    }
}
