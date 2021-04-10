package io.github.notfoundname.xpwars.api.special;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RemoteTNT extends SpecialItem {

    int getFuseTicks();

    Block getBlock();

    List<Location> getLocations();

    void addBlockToLocations();
    
}
