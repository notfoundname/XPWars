package io.github.notfoundname.xpwars.api.features.specials;

import org.bukkit.block.Block;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface Trampoline extends SpecialItem {

    int getYVelocity();

    Block getBlock();
}
