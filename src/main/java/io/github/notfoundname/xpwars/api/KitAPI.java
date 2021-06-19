package io.github.notfoundname.xpwars.api;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public interface KitAPI {

    String getName();

    ItemStack getDisplayIcon();

    List<ItemStack> getItems();

    int getPrice();

    String getPriceType();

    boolean isGivenOnRespawn();

    int getRespawnCooldown();

}
