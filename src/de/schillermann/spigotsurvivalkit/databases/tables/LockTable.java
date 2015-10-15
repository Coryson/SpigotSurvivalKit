package de.schillermann.spigotsurvivalkit.databases.tables;

import de.schillermann.spigotsurvivalkit.entities.BlockLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;

/**
 *
 * @author Mario Schillermann
 */
final public class LockTable extends Table {

    public LockTable(Connection database) {
        super(database, "lock");
    }
 
    public boolean insertLock(
        BlockLocation location,
        Material blockType,
        UUID player
    ) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (location_hash, block_material, world_uuid,"+
            " location_x, location_y, location_z," +
            " player_uuid, created)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, location.hashCode());
            stmt.setString(2, blockType.toString());
            stmt.setString(3, location.getWorld().toString());
            stmt.setInt(4, location.getX());
            stmt.setInt(5, location.getY());
            stmt.setInt(6, location.getZ());
            stmt.setString(7, player.toString());
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
    
    public List<BlockLocation> selectLockLocationFromPlayer(
        UUID player,
        Material block
    ) {
        
        String sql =
            "SELECT world_uuid, location_x, location_y, location_z" +
            " FROM " + this.table +
            " WHERE player_uuid = ? AND block_material = ?";
        
        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, player.toString());
            stmt.setString(2, block.toString());
            ResultSet rs = stmt.executeQuery();

            List<BlockLocation> blockList = new ArrayList<>();
            
            while(rs.next()) {
                blockList.add(
                    new BlockLocation(
                        UUID.fromString(rs.getString("world_uuid")),
                        rs.getInt("location_x"),
                        rs.getInt("location_y"),
                        rs.getInt("location_z")
                    )
                );
            }
            return blockList;
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
    
    public UUID selectPlayerUuidFromLock(BlockLocation location) {
        
        String sql =
            "SELECT player_uuid" +
            " FROM " + this.table +
            " WHERE location_hash = ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, location.hashCode());
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
    
    public boolean deleteLock(BlockLocation location) {
        
        String sql = "DELETE FROM " + this.table + " WHERE location_hash = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, location.hashCode());
            
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
            "location_hash INTEGER NOT NULL PRIMARY KEY," +
            "block_material TEXT NOT NULL," +
            "world_uuid TEXT NOT NULL," +
            "location_x INTEGER NOT NULL," +
            "location_y INTEGER NOT NULL," +
            "location_z INTEGER NOT NULL," +
            "player_uuid TEXT NOT NULL," +
            "created INTEGER NOT NULL" +
            ");"
        );
    }
}
