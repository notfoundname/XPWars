package nfn11.xpwars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

		checkOrSetConfig(modify, "version", 2);

		if (config.getInt("version") != 2) {
			file.renameTo(new File(dataFolder, "config_backup.yml"));
			Bukkit.getServer().getLogger()
					.info("[XPWars] Your XPWars configuration file was backed up. Please transfer values.");
			loadDefaults();
			return;
		}

		checkOrSetConfig(modify, "features.level-system", false);
		checkOrSetConfig(modify, "features.games-gui", false);
		checkOrSetConfig(modify, "features.action-bar-messages", false);
		checkOrSetConfig(modify, "features.permission-to-join-game", false);

		checkOrSetConfig(modify, "permission-to-join-game.message",
				"You don't have permission %perm% to join arena %arena%!");
		checkOrSetConfig(modify, "permission-to-join-game.arenas", new HashMap<String, String>() {
			{
				put("ArenaNameCaseSensetive", "bw.arenanamepermissionanynameyouwant");
				put("Pancake", "bw.allow.pancake");
			}
		});

		checkOrSetConfig(modify, "action-bar-messages.in-lobby", "Your team: %team% [%pl_t%/%mxpl_t%]");
		checkOrSetConfig(modify, "action-bar-messages.in-game-alive", "Your team: %bed% %team%");
		checkOrSetConfig(modify, "action-bar-messages.in-game-spectator", "You are spectator!");

		checkOrSetConfig(modify, "level.messages.maxreached", "&cYou can't have more than %max% levels!");

		checkOrSetConfig(modify, "level.percentage.give-from-killed-player", 33);
		checkOrSetConfig(modify, "level.percentage.keep-from-death", 33);
		checkOrSetConfig(modify, "level.maximum-xp", 1000);
		checkOrSetConfig(modify, "level.sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
		checkOrSetConfig(modify, "level.sound.volume", 1);
		checkOrSetConfig(modify, "level.sound.pitch", 1);

		checkOrSetConfig(modify, "level.per-arena-settings", new HashMap<String, HashMap<String, Object>>() {
			{
				put("ArenaNameCaseSensetive", new HashMap<String, Object>() {
					{
						put("percentage.give-from-killed-player", 100);
						put("percentage.keep-from-death", 0);
						put("maximum-xp", 0);
						put("messages.maxreached", "&loof");
						put("sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
						put("sound.volume", 1);
						put("sound.pitch", 2);
						for (String key : resources.getKeys(false)) {
							put("spawners." + key, 10);
						}
					}
				});
			}
		});

		checkOrSetConfig(modify, "level.spawners", new HashMap<String, Object>() {
			{
				for (String key : resources.getKeys(false)) {
					put(key, 3);
				}
			}
		});

		checkOrSetConfig(modify, "games-gui.permission", "xpwars.gamesgui");

		checkOrSetConfig(modify, "games-gui.title", "&rGames [&e%free%&7/&6%total%&r]");

		checkOrSetConfig(modify, "games-gui.itemstack.WAITING",
				"GREEN_WOOL;1;&a%arena% &f[%players%/%maxplayers%];Waiting %time_left%");
		checkOrSetConfig(modify, "games-gui.itemstack.RUNNING",
				"RED_WOOL;1;&c%arena% &f[%players%/%maxplayers%];Running :) Time left: %time_left%");
		checkOrSetConfig(modify, "games-gui.itemstack.GAME_END_CELEBRATING",
				"BLUE_WOOL;1;&9%arena% &f[%players%/%maxplayers%];Ended");
		checkOrSetConfig(modify, "games-gui.itemstack.REBUILDING",
				"GRAY_WOOL;1;&7%arena% &f[%players%/%maxplayers%];rebuilding");
		checkOrSetConfig(modify, "games-gui.itemstack.DISABLED",
				"BLACK_WOOL;1;&0%arena% &f[%players%/%maxplayers%];disabled");

		checkOrSetConfig(modify, "specials.remote-tnt.fuse-ticks", 100);
		checkOrSetConfig(modify, "specials.remote-tnt.detonator-itemstack", "TRIPWIRE_HOOK;1;&eDetonator");
		/*
		 * checkOrSetConfig(modify, "specials.trampoline.remove-when-used", true);
		 * checkOrSetConfig(modify, "specials.trampoline.y-check", 0);
		 * checkOrSetConfig(modify, "specials.trampoline.velocity", 5.0);
		 */
		checkOrSetConfig(modify, "specials.throwable-tnt.velocity", 5.0);
		checkOrSetConfig(modify, "specials.throwable-tnt.fuse-ticks", 5);

		checkOrSetConfig(modify, "placeholders.waiting", "&aWaiting...");
		checkOrSetConfig(modify, "placeholders.starting", "&eArena is starting in %time%!");
		checkOrSetConfig(modify, "placeholders.running", "&cRunning! Time left: %time%");
		checkOrSetConfig(modify, "placeholders.end-celebration", "&9Game ended!");
		checkOrSetConfig(modify, "placeholders.rebuilding", "&7Rebuilding...");

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
		return ChatColor.translateAlternateColorCodes('&',
				XPWars.getConfigurator().config.getString(string, defaultString));
	}

	public List<String> getStringList(String string) {
		List<String> list = new ArrayList<>();
		for (String s : XPWars.getConfigurator().config.getStringList(string)) {
			s = ChatColor.translateAlternateColorCodes('&', s);
			list.add(s);
		}
		return list;
	}

	public boolean getBoolean(String string, boolean defaultBoolean) {
		return XPWars.getConfigurator().config.getBoolean(string, defaultBoolean);
	}

	public int getInt(String string, int defaultInt) {
		return XPWars.getConfigurator().config.getInt(string, defaultInt);
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
