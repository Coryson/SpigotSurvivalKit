package de.schillermann.spigotsurvivalkit.cache;

import de.schillermann.spigotsurvivalkit.databases.tables.HelperTable;
import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author Mario Schillermann
 */
final public class HelperCache {
    
    final HelperTable helper;
    final int cacheSize;
    Integer[] playerhelperHashList;
    int index;
    
    public HelperCache(HelperTable helper, int cacheSize) {
        
        this.helper = helper;
        this.cacheSize = cacheSize;
        this.clear();
    }
    
    public void clear() {
        this.playerhelperHashList = new Integer[this.cacheSize];
        this.index = 0;
    }
    
    public boolean hasPlayerThisHelper(UUID player, UUID helper) {
        
        int isExists = player.hashCode() - helper.hashCode();
        int notExists = helper.hashCode() - player.hashCode();

        Boolean helperExists =
            Arrays.asList(this.playerhelperHashList).contains(isExists);

        if(helperExists) return true;

        Boolean helperNotExists =
            Arrays.asList(this.playerhelperHashList).contains(notExists);

        if(helperNotExists) return false;

        if(this.index == this.cacheSize) this.index++;

        if(this.helper.hasPlayerThisHelper(player, helper)) {
            this.playerhelperHashList[this.index++] = isExists;
            return true;
        }
        
        this.playerhelperHashList[this.index++] = notExists;
        return false;
    }
}
