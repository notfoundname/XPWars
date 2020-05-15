package org.nfn11.bwaddon;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nfn11.bwaddon.commands.SBWACommand;
import org.nfn11.bwaddon.commands.SBWACommandExecutor;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.commands.BwCommandsExecutor;

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
            Bukkit.getLogger().info("Succesfully registered PlaceholderAPI!");
        }
		
		commands = new HashMap<>();
		new SBWACommand();
		SBWACommandExecutor cmd = new SBWACommandExecutor();
		getCommand("sbwa").setExecutor(cmd);
        getCommand("sbwa").setTabCompleter(cmd);
		
	}
	
	public static Configurator getConfigurator() {
        return instance.configurator;
    }

	public static BwAddon getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	public static HashMap<String, BaseCommand> getCommands() {
        return instance.commands;
    }
}
