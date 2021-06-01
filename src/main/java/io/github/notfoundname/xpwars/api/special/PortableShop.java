package io.github.notfoundname.xpwars.api.special;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface PortableShop extends SpecialItem {

    EntityType getEntityType();

    Location getShopLocation();

    int getDuration();

    ItemStack getItemStack();

    String getShopFile();

    String getShopName();

    boolean isEnabledCustomName();

    boolean isAllowedKilling();

    boolean isBaby();

    void run();

}
