package nfn11.xpwars;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

import me.clip.placeholderapi.PlaceholderAPI;
import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.commands.GamesCommand;
import nfn11.xpwars.commands.XPWarsCommand;
import nfn11.xpwars.inventories.GamesInventory;
import nfn11.xpwars.inventories.LevelShop;
import nfn11.xpwars.listener.XPWarsPlayerListener;
import nfn11.xpwars.placeholderapi.PlaceholderAPIHook;
import nfn11.xpwars.special.listener.RegisterSpecialListeners;
import nfn11.xpwars.utils.XPWarsUtils;

public class XPWars extends JavaPlugin implements Listener {

	private static XPWars instance;
	private Configurator configurator;
	private HashMap<String, BaseCommand> commands;

	@Override
	public void onEnable() {
		instance = this;

		configurator = new Configurator(this);
		configurator.loadDefaults();

		InventoryListener.init(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		new XPWarsUtils();
		new LevelShop();
		new XPWarsPlayerListener();
		new RegisterSpecialListeners();
		new ActionBarAPI();
		new XPWarsCommand();
		new GamesInventory();
		new GamesCommand();
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlaceholderAPIHook().register();
			XPWarsUtils.xpwarsLog("&aSuccesfully registered PlaceholderAPI!");
		} else getConfigurator().config.set("features.placeholder-api", false);

		commands = new HashMap<>();

		XPWarsUtils.xpwarsLog("&aLoaded XPWars &2"
				+ Bukkit.getServer().getPluginManager().getPlugin("XPWars").getDescription().getVersion() + "&a!");
		XPWarsUtils.xpwarsLog("&aXPWars addon by &enotfoundname11");
		XPWarsUtils.xpwarsLog("&9https://github.com/notfoundname/XPWars");
		XPWarsUtils.xpwarsLog("&eType &6/bw xpwars help &eto show available commands.");
		XPWarsUtils.xpwarsLog("&c&lMake sure you use latest development build! Check it on Github Actions.");
	}

	@EventHandler
	public void onBwReload(PluginEnableEvent event) {
		String plugin = event.getPlugin().getName();
		if (plugin.equalsIgnoreCase("BedWars")) {
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
					&& new PlaceholderAPIHook().isRegistered()) {
				PlaceholderAPI.unregisterExpansion(new PlaceholderAPIHook());
			}
			Bukkit.getServer().getPluginManager().disablePlugin(XPWars.getInstance());
			Bukkit.getServer().getPluginManager().enablePlugin(XPWars.getInstance());
		}
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
