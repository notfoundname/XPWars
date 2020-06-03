package nfn11.xpwars.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;

public class SpecialItemUtils {

	public static int getIntFromProperty(String name, FileConfiguration config, String fallback,
			BedwarsApplyPropertyToBoughtItem event) {
		try {
			return event.getIntProperty(name);
		} catch (NullPointerException e) {
			return config.getInt(fallback);
		}
	}

	public static double getDoubleFromProperty(String name, FileConfiguration config, String fallback,
			BedwarsApplyPropertyToBoughtItem event) {
		try {
			return event.getDoubleProperty(name);
		} catch (NullPointerException e) {
			return config.getDouble(fallback);
		}
	}

	public static boolean getBooleanFromProperty(String name, FileConfiguration config, String fallback,
			BedwarsApplyPropertyToBoughtItem event) {
		try {
			return event.getBooleanProperty(name);
		} catch (NullPointerException e) {
			return config.getBoolean(fallback);
		}
	}

	public static String getStringFromProperty(String name, FileConfiguration config, String fallback,
			BedwarsApplyPropertyToBoughtItem event) {
		try {
			return event.getStringProperty(name);
		} catch (NullPointerException e) {
			return config.getString(fallback);
		}
	}
	
	public static ItemStack getStackFromProperty(String name, FileConfiguration config, String fallback,
			BedwarsApplyPropertyToBoughtItem event) {
		try {
			return (ItemStack) event.getProperty(name);
		} catch (NullPointerException e) {
			return config.getConfigurationSection("").getItemStack(fallback);
		}
	}
}
