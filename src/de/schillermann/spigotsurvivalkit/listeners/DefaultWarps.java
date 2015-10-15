package de.schillermann.spigotsurvivalkit.listeners;

import de.schillermann.spigotsurvivalkit.entities.WarpLocation;

/**
 *
 * @author Mario Schillermann
 */
final public class DefaultWarps {
    
    final private WarpTarget warpRespawn;
    
    final private WarpTarget warpFirstJoin;
    
    public DefaultWarps(WarpLocation firstJoin, WarpLocation respawn) {
        
        if(firstJoin == null)
            this.warpFirstJoin = null;
        else
            this.warpFirstJoin = new WarpTarget(firstJoin);
        
        if(respawn == null)
            this.warpRespawn = null;
        else
            this.warpRespawn = new WarpTarget(respawn);
    }
   
    public WarpTarget getWarpFirstJoin() {
        return this.warpFirstJoin;
    }
    
    public WarpTarget getWarpRespawn() {
        return this.warpRespawn;
    }
}
