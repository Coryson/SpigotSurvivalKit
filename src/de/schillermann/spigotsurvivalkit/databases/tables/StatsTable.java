package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 
    public boolean insertStats(UUID player, int money) {
        
        String sql =
            "INSERT INTO " + this.table +
            " (player_uuid, money)" +
            " VALUES (?, ?)";
 
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);

            stmt.setString(1, player.toString());
            stmt.setInt(2, money);
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
    
    public Map<UUID,Integer> selectRichestPlayer(int number) {
        
        String sql =
            "SELECT player_uuid, money" +
            " FROM " + this.table +
            " ORDER BY money DESC" +
            " LIMIT ?";

        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setInt(1, number);
            ResultSet rs = stmt.executeQuery();
            
            HashMap<UUID,Integer> playerMap = new HashMap<>();
            
            while(rs.next()) {
                playerMap.put(
                    UUID.fromString(rs.getString("player_uuid")),
                    rs.getInt("money")
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
    
    public void clearTable() {
        
        String sql = "DELETE FROM " + this.table;
        
        try {    
            Statement stmt = this.database.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            
            Bukkit.getLogger().log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
        }
    }
    
    public boolean createTable() {
        
        return super.createTable(
            "CREATE TABLE IF NOT EXISTS " + this.table +
            "(" +
            "player_uuid TEXT NOT NULL PRIMARY KEY," +
            "money INTEGER NOT NULL" +
            ");"
        );
    }
}
