package io.github.notfoundname.xpwars.api.features.specials;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface ThrowableTNT extends SpecialItem {

    int getFuseTicks();

    void run();

}
