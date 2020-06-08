package nfn11.xpwars.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.SpecialItemUtils;

public class ThrowableTNTListener implements Listener {

	private static final String THROWABLE_TNT_PREFIX = "Module:ThrowableTnt:";

	@EventHandler
	public void onThrowableTntBuy(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("throwabletnt")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
		}
	}

	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return THROWABLE_TNT_PREFIX
				+ SpecialItemUtils.getIntFromProperty("fuse-ticks", XPWars.getConfigurator().config,
						"specials.throwable-tnt.fuse-ticks", event)
				+ ":" 
				+ SpecialItemUtils.getIntFromProperty("y-velocity", XPWars.getConfigurator().config,
						"specials.throwable-tnt.y-velocity", event);
	}
}
