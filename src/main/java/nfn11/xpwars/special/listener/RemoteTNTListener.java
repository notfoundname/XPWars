package nfn11.xpwars.special.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBuildBlock;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.SpecialItemUtils;

public class RemoteTNTListener implements Listener {
	private static final String REMOTE_TNT_PREFIX = "Module:RemoteTnt:";

	@EventHandler
	public void onRemoteTntBuy(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("remotetnt")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
		}

	}

	@EventHandler
	public void onTntPlace(BedwarsPlayerBuildBlock event) {
		Player player = event.getPlayer();
		if (event.isCancelled())
			return;

		ItemStack tnt = event.getItemInHand();
		String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(tnt, REMOTE_TNT_PREFIX);
		if (unhidden != null) {
			boolean damage_placer = Boolean.parseBoolean(unhidden.split(":")[2]);
			int fuse_ticks = Integer.parseInt(unhidden.split(":")[3]);
		}

	}

	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return REMOTE_TNT_PREFIX
				+ SpecialItemUtils.getBooleanFromProperty("damage-placer", XPWars.getConfigurator().config,
						"specials.remote-tnt.damage-placer", event)
				+ ":" + SpecialItemUtils.getIntFromProperty("fuse-ticks", XPWars.getConfigurator().config,
						"specials.remote-tnt.fuse-ticks", event);
	}
}
