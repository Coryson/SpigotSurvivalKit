package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 *
 * @author Mario Schillermann
 */
final public class StatsTable extends Table {

    public StatsTable(Connection database) {
        super(database, "stats");
    }
 
    public boolean insertStats(UUID player, int diamond) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (player_uuid, diamond, update)" +
            " VALUES (?, ?, ?, strftime('%s','now'))";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, player.toString());
            stmt.setInt(2, diamond);
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
    
    public boolean updateStats(UUID player, int diamond) {
        
        String sql =
            "UPDATE " + this.table +
            " SET diamond = ?, update = strftime('%s','now') " +
            " WHERE player_uuid = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setInt(1, diamond);
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
    
    public Map<UUID,Integer> selectRichestPlayer(int number) {
        
        String sql =
            "SELECT player_uuid, diamond FROM " + this.table +
            " ORDER BY diamond DESC" +
            " LIMIT ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, number);
            ResultSet rs = stmt.executeQuery();
            
            HashMap<UUID,Integer> playerMap = new HashMap<>();
            
            while(rs.next()) {
                playerMap.put(
                    UUID.fromString(rs.getString("player_uuid")),
                    rs.getInt("diamond")
                );
            }
            return playerMap;
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
    
    public UUID selectPlayer(UUID player) {
        
        String sql =
            "SELECT diamond FROM " + this.table + " WHERE player_uuid = ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, player.toString());
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
    
    public boolean updateStats(UUID player, int diamond, int plot) {
        return true;
    }
    
    public boolean createTable() {
        
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "player_uuid TEXT NOT NULL PRIMARY KEY," +
            "diamond INTEGER NOT NULL," +
            "updated INTEGER NOT NULL" +
            ");"
        );
    }
}
