package io.github.notfoundname.xpwars.utils;

import io.github.notfoundname.xpwars.XPWars;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import javax.annotation.Nullable;
import java.util.*;

public class KitUtils {

    private static HashMap<String, Kit> kitMap;

    @SuppressWarnings("unchecked")
    public static void updateKits() {
        XPWars.getConfigurator().config.getMapList("kits.list").forEach(map ->
                kitMap.put( (String) map.get("name"), new Kit(
                        (String) map.get("name"),
                        StackParser.parse(map.get("kit-icon")),
                        StackParser.parseAll((Collection<Object>) map.get("items")),
                        (int) map.get("price"),
                        (String) map.get("price-type"),
                        (boolean) map.get("return-on-respawn")
                )));
    }

    public static HashMap<String, Kit> getKits() {
        return kitMap;
    }

    @Nullable
    public static Kit getKit(String name) {
        return kitMap.get(name);
    }

    public static void giveKit(Player player, Kit kit) {
        kit.getItems().forEach(itemStack -> player.getInventory().addItem(itemStack));
    }

    public static void addKitToPlayer(Player player, Kit kit) {
        XPWars.getConfigurator().kitConfig.set(player.getUniqueId().toString(), kit);
    }

    public static boolean hasBoughtKit(Player player, Kit kit) {
        return false;
    }

    public static boolean hasBoughtKit(Player player, String kitName) {
        return false;
    }

    public static class Kit {

        private String name, priceType;
        private int price;
        private ItemStack icon;
        private List<ItemStack> items;
        private boolean giveOnRespawn;

        public Kit(String name, ItemStack icon, List<ItemStack> items, int price, String priceType, boolean giveOnRespawn) {
            this.name = name;
            this.icon = icon;
            this.items = items;
            this.price = price;
            this.priceType = priceType;
            this.giveOnRespawn = giveOnRespawn;
        }

        public String getName() {
            return name;
        }

        public ItemStack getIcon() {
            return icon;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        public int getPrice() {
            return price;
        }

        public String getPriceType() {
            return priceType;
        }

        public boolean giveOnRespawn() {
            return giveOnRespawn;
        }
    }

}
