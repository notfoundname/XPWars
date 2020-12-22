package nfn11.xpwars.special.api;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface ThrowableTNT extends SpecialItem {

    int getFuseTicks();

    int getYVelocity();
    
    void throwTnt();

    ItemStack getItem();
    
}
