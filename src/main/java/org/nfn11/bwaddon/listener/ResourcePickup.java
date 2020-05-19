package org.nfn11.bwaddon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.BedwarsAPI;

public class ResourcePickup implements Listener {
	
	org.nfn11.bwaddon.BwAddon plugin;
	private BedwarsAPI api;
    public ResourcePickup(org.nfn11.bwaddon.BwAddon plugin) {
    	this.plugin = plugin;
    }
    
	@EventHandler
	public void onResourcePickup(EntityPickupItemEvent e) {
		api = BedwarsAPI.getInstance();
		if(!api.isEntityInGame(e.getEntity())) return;
		
		if (!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		
		ItemStack stack = e.getItem().getItemStack();
		
		
	}
}
