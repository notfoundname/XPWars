package nfn11.xpwars.special.listener;

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

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.Vouncher;
import nfn11.xpwars.utils.SpecialItemUtils;

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

		String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(item, VOUNCHER_PREFIX);
		if (unhidden != null && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
			int levels = Integer.parseInt(unhidden.split(":")[2]);
			
			new Vouncher(game, player, game.getTeamOfPlayer(player), levels);
			
			int defmax = XPWars.getConfigurator().getInt("level.maximum-xp", 0);
			int max = XPWars.getConfigurator().getInt("level.games." + game.getName() + ".maximum-xp", defmax);

			if ((player.getLevel() + levels) > max) {
				ActionBarAPI.sendActionBar(player,
						XPWars.getConfigurator().config
								.getString("level.per-arena-settings." + game.getName() + ".messages.maxreached",
										XPWars.getConfigurator().config.getString("level.messages.maxreached"))
								.replace("%max%", Integer.toString(max)));
			} else {
				player.setLevel(player.getLevel() + levels);
			}

		}
	}

	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return VOUNCHER_PREFIX + SpecialItemUtils.getIntFromProperty("levels", XPWars.getConfigurator().config,
				"specials.vouncher.levels", event);
	}
}
