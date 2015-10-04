package de.schillermann.spigotsurvivalkit.cache;

import com.google.common.primitives.Ints;
import de.schillermann.spigotsurvivalkit.databases.tables.HelperTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

/**
 *
 * @author Mario Schillermann
 */
final public class HelperCache {
    
    final private HelperTable helper;
    final private int cacheSize;
    private int[] playerHelperHashList;
    private HashMap<Integer, List<String>> helperList;
    private int index;

    public HelperCache(HelperTable helper, int cacheSize) {
        
        this.helper = helper;
        this.cacheSize = cacheSize;
        this.clear();
    }
    
    public void clear() {
        this.playerHelperHashList = new int[this.cacheSize];
        this.helperList = new HashMap<>();
        this.index = 0;
    }
    
    public int editHelperFromPlayer(UUID player, UUID helper) {
        
        int result = -1;
        
        if(this.helper.deleteHelper(player, helper))
            result = 1;   
        else if(this.helper.insertHelper(player, helper))
            result = 2;
        
        this.clear();
        return result;
    }
    
    public List<String> getHelperListFromPlayer(UUID player) {
        
        Integer playerHash = player.hashCode();
        
        if(this.helperList.size() > this.cacheSize)
            this.helperList.clear();
        
        if(this.helperList.containsKey(playerHash))
            return this.helperList.get(playerHash);

        
        List<UUID> helperUuidList = this.helper.selectHelperListFromPlayer(player);
        List<String> helperNameList = new ArrayList<>();
        
        for (UUID uuid : helperUuidList)
            helperNameList.add(Bukkit.getOfflinePlayer(uuid).getName());
        
        this.helperList.put(playerHash, helperNameList);
        
        return helperNameList;
    }
    
    public boolean hasPlayerThisHelper(UUID player, UUID helper) {
        
        int isHelper = player.hashCode() - helper.hashCode();
        int noHelper = helper.hashCode() - player.hashCode();

        if(isHelper == 0 || Ints.contains(this.playerHelperHashList, noHelper))
            return false;
        
        if(Ints.contains(this.playerHelperHashList, isHelper))
            return true;

        if(this.index == this.cacheSize)
            this.index = 0;
        else
            this.index++;

        if(this.helper.hasPlayerThisHelper(player, helper)) {
            this.playerHelperHashList[this.index] = isHelper;
            return true;
        }

        this.playerHelperHashList[this.index] = noHelper;
        return false;
    }
}
