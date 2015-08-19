package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.entities.PlayerMenu;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
/**
 *
 * @author Mario Schillermann
 */
final public class MenuListener implements Listener {
   
    public MenuListener() {
        
    }
        
    @EventHandler
    public void onOpenInventory(PlayerInteractEvent event) {
        
        if(
            event.getAction() != Action.RIGHT_CLICK_BLOCK &&
            event.getPlayer().getType() != EntityType.PLAYER
        ) return;
        
        Player player = (Player)event.getPlayer();
        event.getPlayer().openInventory(PlayerMenu.getInventory(player));
    }
}
