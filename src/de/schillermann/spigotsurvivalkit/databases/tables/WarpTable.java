package de.schillermann.spigotsurvivalkit.databases.tables;

import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpItem;
import de.schillermann.spigotsurvivalkit.databases.tables.entities.WarpLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
final public class WarpTable extends Table {
    
    public WarpTable(Connection database) {
        super(database, "warp");
    }
    
    public boolean insertWarp(
        String warpName,
        String warpDescription,
        Material warpItem,
        UUID world,
        double locationX,
        double locationY,
        double locationZ,
        UUID creator
    ) {
        
        String sql =
            "INSERT INTO " + this.table +
            " (warp_name, warp_description, warp_item, world," +
            "location_x, location_y, location_z, creator_uuid, created)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, warpName);
            stmt.setString(2, warpDescription);
            stmt.setString(3, warpItem.name());
            stmt.setString(4, world.toString());
            stmt.setDouble(5, locationX);
            stmt.setDouble(6, locationY);
            stmt.setDouble(7, locationZ);
            stmt.setString(8, creator.toString());
            
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
    
    public boolean deleteWarp(String name) {
        
        String sql = "DELETE FROM " + this.table + " WHERE warp_name = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, name);
            
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
    
    public WarpLocation selectWarp(String warpName) {
        
        String sql =
            "SELECT warp_description, location_x, location_y, location_z, world" +
            " FROM " + this.table +
            " WHERE warp_name = ?";
        
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, warpName);
            ResultSet rs = stmt.executeQuery();
       
            if(!rs.next()) return null;
            
            return new WarpLocation(
                UUID.fromString(rs.getString("world")),
                rs.getDouble("location_x"),
                rs.getDouble("location_y"),
                rs.getDouble("location_z"),
                rs.getString("warp_description")
            );
        }
        catch (SQLException e)
        {
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return null;
        }
    }
    
    public List<WarpItem> selectWarpItems() {
        
        String sql =
            "SELECT warp_name, warp_description, warp_item" + 
            " FROM " + this.table;

        try
        {    
            Statement stmt = this.database.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            List<WarpItem> warpList = new ArrayList<>();
                  
            while(rs.next()) {
                
                WarpItem item = new WarpItem(
                    rs.getString("warp_name"),
                    rs.getString("warp_description"),
                    Material.getMaterial(rs.getString("warp_item"))
                );
                warpList.add(item);
            }

            return warpList;
        }
        catch (SQLException e)
        {
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
            "warp_name TEXT NOT NULL PRIMARY KEY," +
            "warp_description TEXT," +
            "warp_item TEXT NOT NULL," +
            "world TEXT NOT NULL," +
            "location_x REAL NOT NULL," +
            "location_y REAL NOT NULL," +
            "location_z REAL NOT NULL," +
            "creator_uuid TEXT NOT NULL," +
            "created INTEGER NOT NULL" +
            ");"
        );
    }
}
