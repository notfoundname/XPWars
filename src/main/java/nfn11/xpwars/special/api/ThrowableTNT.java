package nfn11.xpwars.special.api;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface ThrowableTNT extends SpecialItem {
    
    /**
     * @return Fuse ticks
     */
    int getFuseTicks();

    /**
     * @return How far will TNT throw
     */
    int getYVelocity();
    
    void throwTnt();

    ItemStack getItem();
    
}
