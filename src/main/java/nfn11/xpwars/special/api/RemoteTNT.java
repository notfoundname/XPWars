package nfn11.xpwars.special.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RemoteTNT extends SpecialItem {

    /**
     * @return Fuse ticks
     */
    int getFuseTicks();

    /**
     * @return Block
     */
    Block getBlock();

    /**
     * @return List of placed block locations
     */
    List<Location> getLocations();

    /**
     * @return add block to locations
     */
    void addBlockToLocations();
    
}
