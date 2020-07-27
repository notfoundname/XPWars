package nfn11.xpwars.special.api;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface PortableShop extends SpecialItem {

    int getDuration();
    
    ItemStack getItem();

    void run();

    GameStore getGameStore();

}
