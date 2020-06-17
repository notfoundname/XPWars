package nfn11.xpwars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.commands.XPWarsCommand;
import nfn11.xpwars.inventories.LevelShop;
import nfn11.xpwars.listener.XPWarsPlayerListener;
import nfn11.xpwars.special.listener.RegisterSpecialListeners;

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
		new LevelShop();
		new XPWarsPlayerListener();
		new RegisterSpecialListeners();
		new ActionBarAPI();
		new XPWarsCommand();
		
		commands = new HashMap<>();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new nfn11.xpwars.placeholderapi.PlaceholderAPIHook(this).register();
			log("[XPWars] &aSuccesfully registered PlaceholderAPI!");
		}
		
		log("[XPWars] &aLoaded XPWars &2"
				+ Bukkit.getServer().getPluginManager().getPlugin("XPWars").getDescription().getVersion() + "&a!");
		log("[XPWars] &aXPWars addon by &enotfoundname11");
		log("[XPWars] &9https://github.com/notfoundname/XPWars");
	}
	
	@EventHandler
	public void onBwReload(PluginEnableEvent event) {
		String plugin = event.getPlugin().getName();
		if (plugin.equalsIgnoreCase("BedWars")) {
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

	public static List<String> getShopFileNames() {
		List<String> list = new ArrayList<>();
		List<String> non_allowed = Arrays.asList("config.yml", "sign.yml", "record.yml");
		File[] files = Main.getInstance().getDataFolder().listFiles();

		for (File file : files) {
			if (file.isFile() && !non_allowed.contains(file.getName())) {
				list.add(file.getName());
			}
		}
		return list;
	}

	private void log(String msg) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		Bukkit.getServer().getLogger().info(msg);
	}
	
	public static List<String> getOnlinePlayers() {
		List<String> list = new ArrayList<>();
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			list.add(p.getName());
		}
		return list;
	}
}
