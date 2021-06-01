package io.github.notfoundname.xpwars.special.listener;

import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.game.GameStore;
import org.screamingsandals.bedwars.game.Game;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.special.PortableShop;
import io.github.notfoundname.xpwars.utils.SpecialItemUtils;

public class PortableShopListener implements Listener {
    private static final String PORTABLE_SHOP_PREFIX = "Module:PortableShop:";

    @EventHandler
    public void onPortableShopBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("portableshop"))
            APIUtils.hashIntoInvisibleString(event.getStack(), applyProperty(event));
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        assert Main.isPlayerInGame(player);
        Game game = Main.getPlayerGameProfile(player).getGame();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            for (Entity entity : player.getWorld().getEntities())
                if (entity.hasMetadata(player.getUniqueId().toString()) && entity.hasMetadata("portable-shop"))
                    return;

            ItemStack itemStack = event.getItem();
            String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(itemStack, PORTABLE_SHOP_PREFIX);
            if (unhidden != null) {
                int duration = Integer.parseInt(unhidden.split(":")[2]);
                EntityType entityType = EntityType.valueOf(unhidden.split(":")[3]);
                String shopFile = unhidden.split(":")[4];
                boolean isEnabledCustomName = Boolean.parseBoolean(unhidden.split(":")[5]);
                String shopName = unhidden.split(":")[6];
                boolean isAllowedKilling = Boolean.parseBoolean(unhidden.split(":")[7]);
                boolean isBaby = Boolean.parseBoolean(unhidden.split(":")[8]);

                Location shopLocation = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getX(),
                        event.getClickedBlock().getY() + 1, event.getClickedBlock().getZ());

                PortableShop special = new PortableShop(game, player, game.getTeamOfPlayer(player), itemStack, duration, entityType,
                        shopFile, isEnabledCustomName, shopName, isAllowedKilling, isBaby, shopLocation);
                special.run();
            }
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return PORTABLE_SHOP_PREFIX
                + SpecialItemUtils.getIntFromProperty("duration", XPWars.getConfigurator().config,
                "specials.portable-shop.duration", event)
                + ":"
                + SpecialItemUtils.getStringFromProperty("entityType", XPWars.getConfigurator().config,
                "specials.portable-shop.entityType", event)
                + ":"
                + SpecialItemUtils.getStringFromProperty("shopFile", XPWars.getConfigurator().config,
                "specials.portable-shop.shop-file", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("isEnabledCustomName", XPWars.getConfigurator().config,
                        "specials.portable-shop.isEnabledCustomName", event)
                + ":"
                + SpecialItemUtils.getStringFromProperty("shopName", XPWars.getConfigurator().config,
                "specials.portable-shop.shopName", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("isAllowedKilling", XPWars.getConfigurator().config,
                "specials.portable-shop.isAllowedKilling", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("isBaby", XPWars.getConfigurator().config,
                        "specials.portable-shop.isBaby", event);
    }
}
