package com.notfoundname.xpwars.api.features.specials;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.screamingsandals.bedwars.api.special.SpecialItem;

import java.util.List;

public interface RemoteTNT extends SpecialItem {

    int getFuseTicks();

    Block getBlock();

    List<Location> getLocations();

    void addBlockToLocations();

}
