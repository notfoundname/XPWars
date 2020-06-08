package nfn11.xpwars;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.commands.SBWACommand;
import nfn11.xpwars.inventories.LevelShop;
import nfn11.xpwars.listener.PlayerDeathListener;
import nfn11.xpwars.listener.ResourcePickup;
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

		commands = new HashMap<>();
		new SBWACommand();
		new ResourcePickup();
		new PlayerDeathListener();
		new RegisterSpecialListeners();
		new ActionBarAPI();
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
}
