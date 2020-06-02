package nfn11.xpwars.special.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBuildBlock;
import org.screamingsandals.bedwars.api.special.SpecialItem;

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
        if (event.isCancelled()) return;

        ItemStack tnt = event.getItemInHand();
        String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(tnt, REMOTE_TNT_PREFIX);
        if (unhidden != null) {
            int classID = Integer.parseInt(unhidden.split(":")[2]);

        }

    }
	
	private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
		return REMOTE_TNT_PREFIX
			+ SpecialItemUtils.getBooleanFromProperty(
				    "damage-placer", XPWars.getConfigurator().config, "special.remote-tnt.damage-placer", event) + ":"
			+ SpecialItemUtils.getIntFromProperty(
	                "fuse-ticks", XPWars.getConfigurator().config, "special.remote-tnt.fuse-ticks", event) + ":"
	        + SpecialItemUtils.getBooleanFromProperty(
	                "detonator-allow-drop", XPWars.getConfigurator().config, "special.remote-tnt.detonator-allow-drop", event) + ":"
	        + SpecialItemUtils.getBooleanFromProperty(
	    	        "detonator-material", XPWars.getConfigurator().config, "special.remote-tnt.detonator-material", event) + ":"
	        + SpecialItemUtils.getStringFromProperty(
	                "detonator-name", XPWars.getConfigurator().config, "special.remote-tnt.detonator-name", event);
	    }
}
