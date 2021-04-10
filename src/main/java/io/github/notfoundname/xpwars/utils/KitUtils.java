package io.github.notfoundname.xpwars.utils;

import io.github.notfoundname.xpwars.XPWars;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitUtils {

    @SuppressWarnings("unchecked")
    public static List<Kit> getKits() {
        List<Kit> list = new ArrayList<>();
        XPWars.getConfigurator().config.getMapList("kits.list").forEach(map ->
                list.add(new Kit(
                    map.get("name").toString(),
                    StackParser.parse(map.get("display-icon")),
                    StackParser.parseAll((Collection<Object>) map.get("items")),
                    Integer.parseInt(map.get("price").toString().split(":")[0]),
                    map.get("price-type").toString().split(":")[1],
                    Boolean.parseBoolean(map.get("give-on-respawn").toString()))));
        return list;
    }

    @Nullable
    public static Kit getKit(String name) {
        for (Kit kit : getKits())
            if (name.equals(kit.getName()))
                return kit;
        return null;
    }

    public static void giveKit(Player player, Kit kit) {
        kit.getItems().forEach(itemStack -> player.getInventory().addItem(itemStack));
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
