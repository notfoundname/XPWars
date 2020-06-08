package nfn11.xpwars.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import nfn11.xpwars.XPWars;
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

	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return VOUNCHER_PREFIX
				+ SpecialItemUtils.getIntFromProperty("give-levels", XPWars.getConfigurator().config,
						"specials.vouncher.give-levels", event);
	}
}
