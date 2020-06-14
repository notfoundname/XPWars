package nfn11.xpwars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.commands.XPWarsCommand;
import nfn11.xpwars.inventories.LevelShop;
import nfn11.xpwars.listener.XPWarsPlayerListener;
import nfn11.xpwars.special.listener.RegisterSpecialListeners;

public class XPWars extends JavaPlugin {

	private static XPWars instance;
	private Configurator configurator;
	private HashMap<String, BaseCommand> commands;

	@Override
	public void onEnable() {
		instance = this;

		configurator = new Configurator(this);
		configurator.loadDefaults();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new nfn11.xpwars.placeholderapi.PlaceholderAPIHook(this).register();
			Bukkit.getServer().getLogger().info("[XPWars] Succesfully registered PlaceholderAPI!");
		}

		InventoryListener.init(this);
		new LevelShop();
		new XPWarsCommand();
		new XPWarsPlayerListener();
		new RegisterSpecialListeners();
		new ActionBarAPI();
		commands = new HashMap<>();
	}

	public static Configurator getConfigurator() {
		return instance.configurator;
	}

	public static XPWars getInstance() {
		return instance;
	}

	public static HashMap<String, BaseCommand> getCommands() {
		return instance.commands;
	}
	
	public static List<String> getShopFileNames() {
        List<String> list = new ArrayList<>();
        File[] files = Main.getInstance().getDataFolder().listFiles();

        for (int i = 0; i < files.length; i++) {
        	if (files[i].getName().equals("config.yml")
        			|| files[i].getName().equals("record.yml")
        			|| files[i].getName().equals("sign.yml")) continue;
        	if (files[i].isFile()) {
        		list.add(files[i].getName());
        	}
        }
        
        return list;
    }
}
