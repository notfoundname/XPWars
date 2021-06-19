package io.github.notfoundname.xpwars.kit;

import io.github.notfoundname.xpwars.api.KitAPI;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Kit implements KitAPI {
    private String name, priceType;
    private int price, respawnCooldown;
    private ItemStack icon;
    private List<ItemStack> items;
    private boolean isGivenOnRespawn;

    public Kit(String name, ItemStack icon, List<ItemStack> items, int price, String priceType, boolean isGivenOnRespawn, int respawnCooldown) {
        this.name = name;
        this.icon = icon;
        this.items = items;
        this.price = price;
        this.priceType = priceType;
        this.isGivenOnRespawn = isGivenOnRespawn;
        this.respawnCooldown = respawnCooldown;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return icon;
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getPriceType() {
        return priceType;
    }

    @Override
    public boolean isGivenOnRespawn() {
        return isGivenOnRespawn;
    }

    @Override
    public int getRespawnCooldown() {
        return respawnCooldown;
    }
}
