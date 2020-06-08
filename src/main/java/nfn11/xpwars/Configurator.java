package nfn11.xpwars;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.bukkit.ChatColor;

/*
 * not mine. thanks misat11 & ceph
 */
public class Configurator {

	public File file;
	public FileConfiguration config;

	public final File dataFolder;
	public final XPWars main;

	public Configurator(XPWars main) {
		this.dataFolder = main.getDataFolder();
		this.main = main;
	}

	public void loadDefaults() {
		dataFolder.mkdirs();

		file = new File(dataFolder, "config.yml");

		config = new YamlConfiguration();

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		AtomicBoolean modify = new AtomicBoolean(false);

		ConfigurationSection resources = Main.getConfigurator().config.getConfigurationSection("resources");
		
		checkOrSetConfig(modify, "level.enable", true);
		checkOrSetConfig(modify, "level.percentage.give-from-killed-player", 33);
		checkOrSetConfig(modify, "level.percentage.keep-from-death", 33);
		checkOrSetConfig(modify, "level.maximum-xp", 1000);
		checkOrSetConfig(modify, "level.sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
		checkOrSetConfig(modify, "level.sound.volume", 1);
		checkOrSetConfig(modify, "level.sound.pitch", 1);
		
		checkOrSetConfig(modify, "level.per-arena-settings", new HashMap<String, HashMap<String, Object>>() {{
			put("ArenaNameCaseSensetive", new HashMap<String, Object>() {{
				put("percentage.give-from-killed-player", 100);
				put("percentage.keep-from-death", 0);
				put("maximum-xp", 0);
				put("sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
				put("sound.volume", 1);
				put("sound.pitch", 2);
				for (String key : resources.getKeys(false)) {
					put("spawners." + key, 10);
				}
			}});
		}});
		
		checkOrSetConfig(modify, "level.spawners", new HashMap<String, Object>() {{
			for (String key : resources.getKeys(false)) {
				put(key, 3);
			}
		}});

		checkOrSetConfig(modify, "specials.remote-tnt.fuse-ticks", 100);
		checkOrSetConfig(modify, "specials.remote-tnt.detonator-itemstack", new ItemStack(Material.TRIPWIRE_HOOK));

		checkOrSetConfig(modify, "specials.trampoline.remove-when-used", true);
		checkOrSetConfig(modify, "specials.trampoline.y-check", 0);
		checkOrSetConfig(modify, "specials.trampoline.y-velocity", 5.0);

		checkOrSetConfig(modify, "specials.throwable-tnt.y-velocity", 5.0);

		checkOrSetConfig(modify, "messages.placeholders.waiting", "&aWaiting...");
		checkOrSetConfig(modify, "messages.placeholders.starting", "&eArena is starting in %time%!");
		checkOrSetConfig(modify, "messages.placeholders.running", "&cRunning! Time left: %time%");
		checkOrSetConfig(modify, "messages.placeholders.end-celebration", "&9Game ended!");
		checkOrSetConfig(modify, "messages.placeholders.rebuilding", "&7Rebuilding...");

		checkOrSetConfig(modify, "messages.level.notenoughlevels",
				"&cYou don't have enough levels to buy &r%item%&c! Needed: &e%levels%");
		checkOrSetConfig(modify, "messages.level.nospace",
				"&cYou don't have enough space in your inventory! Free it up!");
		checkOrSetConfig(modify, "messages.level.maxreached", "&cYou can't have more than %max% levels!");

		checkOrSetConfig(modify, "messages.commands.reloaded", "[XPWars] &aReloaded!");
		checkOrSetConfig(modify, "messages.commands.errorreload",
				"[XPWars] &cThere's a error while reloading. Check em in console.");
		checkOrSetConfig(modify, "messages.commands.unknown", "[XPWars] &cUnknown command or wrong usage!");
		checkOrSetConfig(modify, "messages.commands.noperm", "[XPWars] &cYou don't have permission!");
		checkOrSetConfig(modify, "messages.commands.nostore",
				"[XPWars] &cShop file does not exist or contains errors!");

		checkOrSetConfig(modify, "version", 1);

		if (config.getInt("version") != 1) {
			file.renameTo(new File(dataFolder, "config_backup.yml"));
			Bukkit.getServer().getLogger()
					.info("[XPWars] Your XPWars configuration file was backed up. Please transfer values.");
			loadDefaults();
		}

		if (modify.get()) {
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getString(String string, String defaultString) {
		if (config.getConfigurationSection(string) == null) {
			return ChatColor.translateAlternateColorCodes('&', defaultString);
		}
		return ChatColor.translateAlternateColorCodes('&', config.getString(string));
	}

	public boolean getBoolean(String string, boolean defaultBoolean) {
		if (config.getConfigurationSection(string) == null) {
			return defaultBoolean;
		}
		return config.getBoolean(string);
	}

	public int getInt(String string, int defaultInt) {
		if (config.getConfigurationSection(string) == null) {
			return defaultInt;
		}
		return config.getInt(string);
	}

	private void checkOrSetConfig(AtomicBoolean modify, String path, Object value) {
		checkOrSet(modify, this.config, path, value);
	}

	private static void checkOrSet(AtomicBoolean modify, FileConfiguration config, String path, Object value) {
		if (!config.isSet(path)) {
			if (value instanceof Map) {
				config.createSection(path, (Map<?, ?>) value);
			} else {
				config.set(path, value);
			}
			modify.set(true);
		}
	}
}
