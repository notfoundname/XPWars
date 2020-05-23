package org.nfn11.bwaddon;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
/*
 * bruh
 */
public class Configurator {

    public File file;
    public FileConfiguration config;

    public final File dataFolder;
    public final BwAddon main;
    
    public Configurator(BwAddon main) {
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
		
		
        checkOrSetConfig(modify, "fast-death-enabled-games", new ArrayList<String>() {{
        	add("ArenaNameCaseSensetive");
        }});
                
        checkOrSetConfig(modify, "level.games.ArenaNameCaseSensetive.bronze", 5);
        checkOrSetConfig(modify, "level.percentage.give-from-killed-player", 33);
        checkOrSetConfig(modify, "level.percentage.keep-from-death", 33);
        checkOrSetConfig(modify, "level.max-level", 1000);
        checkOrSetConfig(modify, "level.games.ArenaNameCaseSensetive.iron", 15);
        checkOrSetConfig(modify, "level.games.ArenaNameCaseSensetive.gold", 30);
        checkOrSetConfig(modify, "level.default.bronze", 3);
        checkOrSetConfig(modify, "level.default.iron", 10);
        checkOrSetConfig(modify, "level.default.gold", 20);
        checkOrSetConfig(modify, "level.default-enabled-games", new ArrayList<String>() {{
        	add("ArenaNameCaseSensetive");
        }});
                
        checkOrSetConfig(modify, "villager.default.cancel-shop-open", false);
        checkOrSetConfig(modify, "villager.default.console-commands", new ArrayList<String>());
        checkOrSetConfig(modify, "villager.default.player-commands", new ArrayList<String>());
        checkOrSetConfig(modify, "villager.enabled-arenas.ArenaNameCaseSensetive.cancel-shop-open", false);
        checkOrSetConfig(modify, "villager.enabled-arenas.ArenaNameCaseSensetive.console-commands", new ArrayList<String>() {{
        	add("say Yes! %player% really did it!");
        	add("say &cYou can use this to replace default shop with some GUI plugin.");
        }});
        checkOrSetConfig(modify, "villager.enabled-arenas.ArenaNameCaseSensetive.player-commands", new ArrayList<String>() {{
        	add("say Hey guys! It's me, %player%! And I just opened a shop!");
        }});
                
        checkOrSetConfig(modify, "special.remote-tnt.damage-placer", true);
        checkOrSetConfig(modify, "special.remote-tnt.fuse-ticks", 100);
        checkOrSetConfig(modify, "special.remote-tnt.detonator.allow-drop", false);
        checkOrSetConfig(modify, "special.remote-tnt.detonator.material", "TRIPWIRE_HOOK");
        checkOrSetConfig(modify, "special.remote-tnt.detonator.name", "&fDetonate all placed TNTs &7(Right-click)");
                
        checkOrSetConfig(modify, "special.trampoline.remove-when-used", true);
        checkOrSetConfig(modify, "special.trampoline.y-check", 0);
        checkOrSetConfig(modify, "special.trampoline.y-velocity", 5.0);
                
        checkOrSetConfig(modify, "special.throwable-tnt.y-velocity", 5.0);
                
        checkOrSetConfig(modify, "messages.placeholders.waiting", "&aWaiting...");
        checkOrSetConfig(modify, "messages.placeholders.starting", "&eArena is starting in %time%!");
        checkOrSetConfig(modify, "messages.placeholders.running", "&cRunning! Time left: %time%");
        checkOrSetConfig(modify, "messages.placeholders.end-celebration", "&9Game ended!");
        checkOrSetConfig(modify, "messages.placeholders.rebuilding", "&7Rebuilding...");
                
        checkOrSetConfig(modify, "messages.level.max-reached", "&cYou can't carry more than %max% levels!");
        
        checkOrSetConfig(modify, "messages.commands.reloaded", "[SBWA] &aReloaded!");
        checkOrSetConfig(modify, "messages.commands.unknown", "[SBWA] &cUnknown command or wrong usage!");
        checkOrSetConfig(modify, "messages.commands.noperm", "[SBWA] &cYou don't have permission!");
        
        checkOrSetConfig(modify, "version", 1);

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
	
	
	public String getString(String string) {
		return config.getString(string.replace("&", "§"));
	}
	
	public boolean getBoolean(String string) {
		return config.getBoolean(string);
	}
	
	public int getInt(String string) {
		return config.getInt(string);
	}
	
	public List<String> getStringList(String string) {
		return config.getConfigurationSection("").getStringList(string.replace("&", "§"));
	}
	
	public Set<String> getStringKeys(String string) {
		return config.getConfigurationSection(string.replace("&", "§")).getKeys(true);
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
