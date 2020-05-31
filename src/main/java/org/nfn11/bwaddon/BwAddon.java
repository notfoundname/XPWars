package org.nfn11.bwaddon;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nfn11.bwaddon.commands.SBWACommand;
import org.nfn11.bwaddon.inventories.LevelShop;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;

public class BwAddon extends JavaPlugin {
	
	private static BwAddon instance;
	private Configurator configurator;
	private HashMap<String, BaseCommand> commands;
	
	@Override
	public void onEnable() {
		instance = this;
		
		configurator = new Configurator(this);
		configurator.loadDefaults();
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new org.nfn11.bwaddon.placeholderapi.PlaceholderAPIHook(this).register();
            Bukkit.getLogger().info("[SBWA] Succesfully registered PlaceholderAPI!");
        }
		
		InventoryListener.init(this);
		LevelShop shop = new LevelShop();
		
		commands = new HashMap<>();
		new SBWACommand();
	}
	
	public static Configurator getConfigurator() {
        return instance.configurator;
    }

	public static BwAddon getInstance() {
		return instance;
	}
	
	public static HashMap<String, BaseCommand> getCommands() {
        return instance.commands;
    }
}
