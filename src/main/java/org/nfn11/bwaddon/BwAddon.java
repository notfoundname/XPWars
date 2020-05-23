package org.nfn11.bwaddon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nfn11.bwaddon.commands.SBWACommand;
import org.screamingsandals.bedwars.commands.BaseCommand;

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
	
	public static ArrayList<String> getShopFileNames() {
		File folder = new File("plugins/BedWars");
		File[] files = folder.listFiles();
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < files.length; i++) {
			
		 	if (files[i].isFile()) {
		 		list.add(files[i].getName());
		 		if(files[i].getName().equals("config.yml")
		 		|| files[i].getName().equals("stats.yml")
		 		|| files[i].getName().equals("record.yml")
		 		|| files[i].getName().equals("sign.yml")) {
		 			list.remove(files[i].getName());
		 		}
		 	}
		 	else if (files[i].isDirectory()) {}
		}
		return list;
	}
}
