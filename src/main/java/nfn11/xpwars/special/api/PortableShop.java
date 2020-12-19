package nfn11.xpwars.special.api;

import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;
import org.screamingsandals.bedwars.game.GameStore;

public interface PortableShop extends SpecialItem {

    GameStore getGameStore();

    /**
     * @return int duration
     */
    int getDuration();

    /**
     * @return ItemStack
     */
    ItemStack getItem();

    void run();

}
