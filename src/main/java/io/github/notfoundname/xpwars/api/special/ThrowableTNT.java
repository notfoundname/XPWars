package io.github.notfoundname.xpwars.api.special;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface ThrowableTNT extends SpecialItem {

    int getFuseTicks();

    int getYVelocity();
    
    void throwTnt();

    ItemStack getItem();
    
}
