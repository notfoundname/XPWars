package nfn11.xpwars.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.SpecialItemUtils;

public class TrampolineListener implements Listener {
	
	private static final String TRAMPOLINE_PREFIX = "Module:Trampoline:";
	
	@EventHandler
	public void onTrampolineBuy(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("trampoline")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
		}
	}

	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return TRAMPOLINE_PREFIX
				+ SpecialItemUtils.getIntFromProperty("y-velocity", XPWars.getConfigurator().config,
						"specials.trampoline.y-velocity", event)
				+ ":"
				+ SpecialItemUtils.getIntFromProperty("y-check", XPWars.getConfigurator().config,
						"specials.trampoline.y-check", event);
	}
}
