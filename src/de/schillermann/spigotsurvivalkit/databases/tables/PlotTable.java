package de.schillermann.spigotsurvivalkit.databases.tables;

import de.schillermann.spigotsurvivalkit.entities.PlotMetadata;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

/**
 *
 * @author Mario Schillermann
 */
final public class PlotTable extends Table {
    
    public PlotTable(Connection database) {
        super(database, "plot");
    }
    
    public PlotMetadata selectMetadata(Chunk chunk) {
        
        String sql =
            "SELECT owner_uuid, price FROM " + this.table +
            " WHERE chunk_x = ? AND chunk_z = ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, chunk.getX());
            stmt.setInt(2, chunk.getZ());
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new PlotMetadata(
                    UUID.fromString(rs.getString("owner_uuid")),
                    rs.getInt("price")
                );
            }
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
    
    public HashMap<UUID, Integer> selectPlotQuantity() {
        
        String sql =
            "SELECT owner_uuid, COUNT(*) as plot_quantity" +
            " FROM " + this.table +
            " GROUP BY owner_uuid";
        
        try {    
            Statement stmt = this.database.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            HashMap<UUID, Integer> plotList = new HashMap<>();
            
            while(rs.next()) {
                plotList.put(
                    UUID.fromString(rs.getString("owner_uuid")),
                    rs.getInt("plot_quantity")
                );
            }
            
            return plotList;
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
    
    public boolean updatePrice(Chunk chunk, int price) {
        
        String sql =
            "UPDATE " + this.table +
            " SET price = ?, updated = strftime('%s','now')" +
            " WHERE chunk_x = ? AND chunk_z = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, price);
            stmt.setInt(2, chunk.getX());
            stmt.setInt(3, chunk.getZ());
            
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
    
    public boolean updateMetadata(Chunk chunk, PlotMetadata metadata) {
        
        String sql =
            "UPDATE " + this.table +
            " SET owner_uuid = ?, price = ?, updated = strftime('%s','now')" +
            " WHERE chunk_x = ? AND chunk_z = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, metadata.getOwner().toString());
            stmt.setInt(2, metadata.getPrice());
            stmt.setInt(3, chunk.getX());
            stmt.setInt(4, chunk.getZ());
            
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
    
    public boolean insertPlot(Chunk chunk, UUID owner) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (chunk_x, chunk_z, owner_uuid, updated, created)" +
            " VALUES (?, ?, ?, strftime('%s','now'), strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, chunk.getX());
            stmt.setInt(2, chunk.getZ());
            stmt.setString(3, owner.toString());
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
    
    public boolean deletePlot(Chunk chunk) {
        
        String sql =
            "DELETE FROM " + this.table +
            " WHERE chunk_x = ? AND chunk_z = ?";
  
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
    
    public boolean createTable() {
        
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "chunk_x INTEGER NOT NULL," +
            "chunk_z INTEGER NOT NULL," +
            "owner_uuid TEXT NOT NULL," +
            "price INTEGER DEFAULT 0," +
            "updated INTEGER NOT NULL," +
            "created INTEGER NOT NULL," +
            "PRIMARY KEY(chunk_x, chunk_z)" +
            ");"
        );
    }
}
