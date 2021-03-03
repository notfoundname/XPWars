package nfn11.xpwars.special.listener;

import nfn11.xpwars.utils.XPWarsUtils;
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

import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.PortableShop;
import nfn11.xpwars.utils.SpecialItemUtils;

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

            ItemStack item = event.getItem();
            String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(item, PORTABLE_SHOP_PREFIX);
            if (unhidden != null) {
                String shopFile = unhidden.split(":")[2];
                boolean useParent = Boolean.parseBoolean(unhidden.split(":")[3]);
                EntityType entityType = EntityType.valueOf(unhidden.split(":")[4]);
                boolean hasCustomName = Boolean.parseBoolean(unhidden.split(":")[5]);
                String customName = unhidden.split(":")[6];
                int duration = Integer.parseInt(unhidden.split(":")[7]);
                boolean isBaby = Boolean.parseBoolean(unhidden.split(":")[8]);
                String skinName = unhidden.split(":")[9];

                if (duration < 3) {
                    XPWarsUtils.xpwarsLog(player, "Duration is be lower than 3 seconds. Tell this to server staff.");
                    return;
                }

                Location loc = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getX(),
                        event.getClickedBlock().getY() + 1, event.getClickedBlock().getZ());

                PortableShop special = new PortableShop(game, player, game.getTeamOfPlayer(player), duration, item,
                        new GameStore(loc, shopFile, useParent, entityType, customName, hasCustomName, isBaby, skinName));
                special.run();
            }
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return PORTABLE_SHOP_PREFIX
                + SpecialItemUtils.getStringFromProperty("shop-file", XPWars.getConfigurator().config,
                        "specials.portable-shop.shop-file", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("use-parent", XPWars.getConfigurator().config,
                        "specials.portable-shop.use-parent", event)
                + ":"
                + SpecialItemUtils.getStringFromProperty(
                        "entity-type", XPWars.getConfigurator().config, "specials.portable-shop.entity-type", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("enable-custom-name", XPWars.getConfigurator().config,
                        "specials.portable-shop.enable-custom-name", event)
                + ":"
                + SpecialItemUtils.getStringFromProperty(
                        "custom-name", XPWars.getConfigurator().config, "specials.portable-shop.custom-name", event)
                + ":"
                + SpecialItemUtils.getIntFromProperty("duration", XPWars.getConfigurator().config,
                        "specials.portable-shop.duration", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("baby", XPWars.getConfigurator().config,
                        "specials.portable-shop.baby", event)
                + ":"
                + SpecialItemUtils.getBooleanFromProperty("skin-name", XPWars.getConfigurator().config,
                        "specials.portable-shop.skin-name", event);
    }
}
