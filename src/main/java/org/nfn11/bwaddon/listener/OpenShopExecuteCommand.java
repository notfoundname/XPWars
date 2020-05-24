package org.nfn11.bwaddon.listener;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.nfn11.bwaddon.BwAddon;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent.Result;

public class OpenShopExecuteCommand implements Listener {
	private List<String> c_list, p_list;
	private boolean result;
	org.nfn11.bwaddon.BwAddon plugin;
    public OpenShopExecuteCommand(org.nfn11.bwaddon.BwAddon plugin) {
    	this.plugin = plugin;
    }
    
    @EventHandler
    public void onShopOpen(BedwarsOpenShopEvent e) {
    	e.setResult(result ? Result.DISALLOW_LOCKED_FOR_THIS_PLAYER : Result.ALLOW);
    	
    	String name = e.getGame().getName();
    	
    	if (!BwAddon.getConfigurator().getStringKeys("villager.enabled-arenas").contains(name)) {
    		result = BwAddon.getConfigurator().getBoolean("villager.default.cancel-shop-open", true);
    		c_list = BwAddon.getConfigurator().getStringList("villager.default.console-commands");
    		p_list = BwAddon.getConfigurator().getStringList("villager.default.player-commands");
    	} else {
    		result = BwAddon.getConfigurator().getBoolean("villager.enabled-arenas."+name+".cancel-shop-open", true);
    		c_list = BwAddon.getConfigurator().config.getStringList("villager.enabled-arenas." + name + "console-commands");
    		p_list = BwAddon.getConfigurator().config.getStringList("villager.enabled-arenas." + name + "player-commands");
    	}
    	
    	if (c_list.size() != 0) {
    		for (String cmd : c_list) {
        		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", e.getPlayer().getDisplayName()));
        	}
    	}
    	if (p_list.size() != 0) {
    		for (String cmd : p_list) {
        		e.getPlayer().performCommand(cmd.replace("%player%", e.getPlayer().getDisplayName()));
        	}
    	}
    	
    }
}
