package org.nfn11.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BwAddon extends JavaPlugin {
	
	private static BwAddon instance;
	private Configurator configurator;
	
	@Override
	public void onEnable() {
		instance = this;
		
		configurator = new Configurator(this);
		configurator.loadDefaults();
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new org.nfn11.bwaddon.placeholderapi.PlaceholderAPIHook(this).register();
            Bukkit.getLogger().info("Succesfully registered PlaceholderAPI!");
        }
	}
	
	public static Configurator getConfigurator() {
        return instance.configurator;
    }

	public static BwAddon getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
}
