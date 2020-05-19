package org.nfn11.bwaddon.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.nfn11.bwaddon.BwAddon;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

public class SBWAUtils {
	
	public static int toLevel(ItemStack stack, Game game) {
		int count = 0;
		if (stack == null) return count;
		FileConfiguration config = BwAddon.getConfigurator().config;
		BedwarsAPI api = BedwarsAPI.getInstance();
		
		if (config.getList("").contains(game.getName())) return 0;
		
		if (config.getConfigurationSection("level.games").getValues(false).containsKey(game.getName())) {
			
		}

		return count;
	}
}
