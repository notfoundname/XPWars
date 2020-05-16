package org.nfn11.bwaddon;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
/*
 * huh
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
        checkOrSetConfig(modify, "level.games.ArenaNameCaseSensetive.iron", 15);
        checkOrSetConfig(modify, "level.games.ArenaNameCaseSensetive.gold", 30);
        checkOrSetConfig(modify, "level.default.bronze", 3);
        checkOrSetConfig(modify, "level.default.iron", 10);
        checkOrSetConfig(modify, "level.default.gold", 20);
        checkOrSetConfig(modify, "level.default-enabled-games", new ArrayList<String>() {{
        	add("ArenaNameCaseSensetive");
        }});
                
        checkOrSetConfig(modify, "villager.default.cancel-shop-open", false);
        checkOrSetConfig(modify, "villager.default.commands", new ArrayList<String>());
        checkOrSetConfig(modify, "villager.enabled-arenas.ArenaNameCaseSensetive.cancel-shop-open", false);
        checkOrSetConfig(modify, "villager.enabled-arenas.ArenaNameCaseSensetive.commands", new ArrayList<String>() {{
        	add("[player] say Hey guys! It's me, %player%! And I just opened a shop!");
        	add("[console] say Yes! %player% really did it!");
        	add("[console] say &cYou can use this to replace default shop with some GUI plugin.");
        }});
                
        checkOrSetConfig(modify, "special.tnt.damage-placer", true);
        checkOrSetConfig(modify, "special.tnt.fuse-ticks", 100);
        checkOrSetConfig(modify, "special.tnt.detonator-allow-drop", false);
        checkOrSetConfig(modify, "special.tnt.detonator-itemstack", new ItemStack(Material.TRIPWIRE_HOOK));
                
        checkOrSetConfig(modify, "special.trampoline.remove-when-used", true);
        checkOrSetConfig(modify, "special.trampoline.y-check", 0);
        checkOrSetConfig(modify, "special.trampoline.y-velocity", 5.0);
                
        checkOrSetConfig(modify, "special.throwable-tnt.y-velocity", 5.0);
                
        checkOrSetConfig(modify, "messages.placeholders.waiting", "&aWaiting...");
        checkOrSetConfig(modify, "messages.placeholders.starting", "&eArena is starting in %time%!");
        checkOrSetConfig(modify, "messages.placeholders.running", "&cRunning! Time left: %time%");
        checkOrSetConfig(modify, "messages.placeholders.end-celebration", "&9Game ended!");
        checkOrSetConfig(modify, "messages.placeholders.rebuilding", "&7Rebuilding...");
                
        checkOrSetConfig(modify, "messages.commands.reloaded", "[SBWA] &aReloaded!");
        checkOrSetConfig(modify, "messages.commands.unknown", "[SBWA] &cUnknown command!");
        checkOrSetConfig(modify, "messages.commands.noperm", "[SBWA] &cYou don't have permission!");
        checkOrSetConfig(modify, "messages.commands.fastdeath115", "[SBWA] &eFast Death option can't be used on versions above 1.15");

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
