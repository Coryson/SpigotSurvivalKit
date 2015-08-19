package de.schillermann.spigotsurvivalkit.databases.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario Schillermann
 */
final public class HelperTable extends Table {
    
    public HelperTable(Connection database, Logger logger) {
        super(database, "helper", logger);
    }
    
    public boolean insertHelper(UUID player, UUID helper) {
        
        String sql =
            "INSERT OR IGNORE INTO " + this.table +
            " (player_uuid, helper_uuid) " +
            "VALUES (?, ?)";
  
        try {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, player.toString());
            stmt.setString(2, helper.toString());
            stmt.execute();
            
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

    public List<UUID> selectHelperListFromPlayer(UUID player) {
        
        String sql =
            "SELECT helper_uuid FROM " + this.table + " WHERE player_uuid = ?";

        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, player.toString());
            ResultSet rs = stmt.executeQuery();
            
            List<UUID> helperList = new ArrayList<>();
                  
            while(rs.next())
            {
                UUID helperUuid = UUID.fromString(rs.getString("helper_uuid"));      
                helperList.add(helperUuid);
            }
            
            return helperList;
        }
        catch (SQLException e)
        {
            this.logger.log(
                Level.WARNING,
                "{0}: {1}",
                new Object[]{e.getClass().getName(), e.getMessage()}
            );
            return null;
        }
    }
    
    public boolean hasPlayerThisHelper(UUID player, UUID helper) {
        String sql =
            "SELECT helper_uuid FROM " + this.table +
            " WHERE player_uuid = ? AND helper_uuid = ?";

        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            stmt.setString(1, player.toString());
            stmt.setString(2, helper.toString());
            
            return stmt.getMaxRows() > 0;
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
    
    public boolean deleteHelper(UUID player, UUID helper) {
        
        String sql =
            "DELETE FROM " + this.table + " WHERE player_uuid = ? AND helper_uuid = ?";
  
        try
        {    
            PreparedStatement stmt = this.database.prepareStatement(sql);
            
            stmt.setString(1, player.toString());
            stmt.setString(2, helper.toString());
            
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
            "player_uuid TEXT NOT NULL," +
            "helper_uuid TEXT NOT NULL," +
            "created INTEGER NOT NULL," +
            "PRIMARY KEY(player_uuid, helper_uuid)" +
            ");"
        );
    }
}
