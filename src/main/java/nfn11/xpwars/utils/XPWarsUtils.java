package nfn11.xpwars.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;

public class XPWarsUtils {
	
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

	public static void xpwarsLog(String msg) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		msg = "[XPWars] " + msg;
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
