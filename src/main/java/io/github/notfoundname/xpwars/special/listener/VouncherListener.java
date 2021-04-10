package io.github.notfoundname.xpwars.special.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.game.Game;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.special.Vouncher;
import io.github.notfoundname.xpwars.utils.SpecialItemUtils;

public class VouncherListener implements Listener {

    private static final String VOUNCHER_PREFIX = "Module:Vouncher:";

    @EventHandler
    public void onVouncherBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("vouncher")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }
    }

    @EventHandler
    public void onUseVouncher(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Main.isPlayerInGame(player))
            return;
        Game game = Main.getPlayerGameProfile(player).getGame();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (item != null)
            return;
        String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(item, VOUNCHER_PREFIX);
        if (unhidden != null && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            int levels = Integer.parseInt(unhidden.split(":")[2]);

            Vouncher special = new Vouncher(game, player, game.getTeamOfPlayer(player), levels, item);
            special.setLevel();
            event.setCancelled(true);
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return VOUNCHER_PREFIX + SpecialItemUtils.getIntFromProperty("levels", XPWars.getConfigurator().config,
                "specials.vouncher.levels", event);
    }
}
