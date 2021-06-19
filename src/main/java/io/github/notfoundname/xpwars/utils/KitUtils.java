package io.github.notfoundname.xpwars.utils;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.kit.Kit;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import javax.annotation.Nullable;
import java.util.*;

public class KitUtils {

    private static HashMap<String, Kit> kitMap;

    @SuppressWarnings({"unchecked", "a"})
    public static void updateKits() {
        kitMap.clear();
        for (String kitName : XPWars.getConfiguration().getConfigurationSection("kits.list").getKeys(false)) {
            kitMap.put(kitName, new Kit(
                    kitName,
                    StackParser.parse(XPWars.getConfiguration().get
                            ("kits.list." + kitName + ".icon", "BARRIER;1;Your kit icon is broken")),
                    StackParser.parseAll((Collection<Object>) XPWars.getConfiguration().get
                            ("kits.list." + kitName + ".items", "BARRIER;1;Your kit list is broken")),
                    XPWars.getConfiguration().getInt("kits.list." + kitName + ".price", 0),
                    XPWars.getConfiguration().getString("kits.list." + kitName + ".price-type", "score"),
                    XPWars.getConfiguration().getBoolean("kits.list." + kitName + ".return-on-respawn", false),
                    XPWars.getConfiguration().getInt("kits.list." + kitName + ".respawn-cooldown", 30)));
        }
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

    public static boolean isOwnKit(Player player, Kit kit) {
        return false;
    }

    public static boolean isOwnKit(Player player, String kitName) {
        return false;
    }

}
