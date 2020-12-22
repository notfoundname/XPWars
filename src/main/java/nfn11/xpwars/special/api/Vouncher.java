package nfn11.xpwars.special.api;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface Vouncher extends SpecialItem {

    int getLevels();
    
    ItemStack getItem();
    
    void setLevel();
    
}
