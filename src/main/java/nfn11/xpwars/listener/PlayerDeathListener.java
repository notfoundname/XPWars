package nfn11.xpwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;

import nfn11.xpwars.XPWars;

public class PlayerDeathListener implements Listener {
	
	public PlayerDeathListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(BedwarsPlayerKilledEvent event) {
		Player player = event.getPlayer();
		Player killer = event.getKiller();
		
		int player_level = player.getLevel();
		int killer_level = killer.getLevel();
		
		
	}
}
