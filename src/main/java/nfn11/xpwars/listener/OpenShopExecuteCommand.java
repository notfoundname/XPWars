package nfn11.xpwars.listener;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent.Result;

import nfn11.xpwars.XPWars;

public class OpenShopExecuteCommand implements Listener {
	private List<String> c_list, p_list;
	private boolean result;
	nfn11.xpwars.XPWars plugin;
    public OpenShopExecuteCommand(nfn11.xpwars.XPWars plugin) {
    	this.plugin = plugin;
    }
    
    @EventHandler
    public void onShopOpen(BedwarsOpenShopEvent e) {
    	e.setResult(result ? Result.DISALLOW_LOCKED_FOR_THIS_PLAYER : Result.ALLOW);
    	
    	String name = e.getGame().getName();
    	
    	if (!XPWars.getConfigurator().getStringKeys("villager.enabled-arenas").contains(name)) {
    		result = XPWars.getConfigurator().getBoolean("villager.default.cancel-shop-open", true);
    		c_list = XPWars.getConfigurator().getStringList("villager.default.console-commands");
    		p_list = XPWars.getConfigurator().getStringList("villager.default.player-commands");
    	} else {
    		result = XPWars.getConfigurator().getBoolean("villager.enabled-arenas."+name+".cancel-shop-open", true);
    		c_list = XPWars.getConfigurator().config.getStringList("villager.enabled-arenas." + name + "console-commands");
    		p_list = XPWars.getConfigurator().config.getStringList("villager.enabled-arenas." + name + "player-commands");
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
