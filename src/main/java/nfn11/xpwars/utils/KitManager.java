package nfn11.xpwars.utils;

import nfn11.xpwars.XPWars;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KitManager {

    public static List<Kit> getKits() {
        List<Kit> list = new ArrayList<>();

        XPWars.getConfigurator().config.getMapList("kits.list").forEach(map -> {
            list.add(new Kit((String) map.get("name"), StackParser.parse(map.get("display-icon")),
                    StackParser.parseAll((Collection<Object>) map.get("items"))));
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

}
