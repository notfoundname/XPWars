package nfn11.xpwars.utils;

import nfn11.xpwars.XPWars;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitManager {

    public static List<Kit> getKits() {
        List<Kit> list = new ArrayList<>();

        XPWars.getConfigurator().config.getMapList("kits.list").forEach(map -> {
            list.add(new Kit(map.get("name").toString(), StackParser.parse(map.get("display-icon")),
                    StackParser.parseAll((Collection<Object>) map.get("items")), // right now only score, later vault
                    Integer.parseInt(map.get("price").toString()), "score"/*map.get("price-type").toString()*/));
        });
        return list;
    }

    public static Kit getKit(String name) {
        for (Kit kit : getKits()) {
            if (name.equals(kit.getName()))
                return kit;
        };
        return null;
    }

    public static void giveKit(Player player, Kit kit) {
        kit.getItems().forEach(itemStack -> {
            player.getInventory().addItem(itemStack);
        });
    }

    public static class Kit {

        private String name, priceType;
        private int price;
        private ItemStack icon;
        private List<ItemStack> items;

        public Kit(String name, ItemStack icon, List<ItemStack> items, int price, String priceType) {
            this.name = name;
            this.icon = icon;
            this.items = items;
            this.price = price;
            this.priceType = priceType;
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

    }

}
