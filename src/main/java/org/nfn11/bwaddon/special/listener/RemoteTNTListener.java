package org.nfn11.bwaddon.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.nfn11.bwaddon.BwAddon;
import org.nfn11.bwaddon.utils.SpecialItemUtils;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;

public class RemoteTNTListener implements Listener {
	private static final String REMOTE_TNT_PREFIX = "Module:RemoteTnt:";
	
	@EventHandler
    public void onRemoteTntBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("remotetnt")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }

    }
	
	
	
	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return REMOTE_TNT_PREFIX
			+ SpecialItemUtils.getBooleanFromProperty(
				    "damage-placer", BwAddon.getConfigurator().config, "special.remote-tnt.damage-placer", event) + ":"
			+ SpecialItemUtils.getIntFromProperty(
	                "fuse-ticks", BwAddon.getConfigurator().config, "special.remote-tnt.fuse-ticks", event) + ":"
	        + SpecialItemUtils.getBooleanFromProperty(
	                "detonator-allow-drop", BwAddon.getConfigurator().config, "special.remote-tnt.detonator-allow-drop", event) + ":"
	        + SpecialItemUtils.getBooleanFromProperty(
	    	        "detonator-material", BwAddon.getConfigurator().config, "special.remote-tnt.detonator-material", event) + ":"
	        + SpecialItemUtils.getStringFromProperty(
	                "detonator-name", BwAddon.getConfigurator().config, "special.remote-tnt.detonator-name", event);
	    }
}
