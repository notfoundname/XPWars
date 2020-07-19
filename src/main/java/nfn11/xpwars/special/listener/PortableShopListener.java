package nfn11.xpwars.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.SpecialItemUtils;

public class PortableShopListener implements Listener {
	private static final String PORTABLE_SHOP_PREFIX = "Module:PortableShop:";

	@EventHandler
	public void onPortableShopBuy(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("portableshop")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
		}
	}

	@EventHandler
	public void onUse(PlayerInteractEvent event) {

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
				+ ":" + SpecialItemUtils.getBooleanFromProperty("baby", XPWars.getConfigurator().config,
						"specials.portable-shop.baby", event);
	}
}
