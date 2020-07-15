package nfn11.xpwars.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import nfn11.xpwars.XPWars;

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

	public static boolean isNewVersion() {
		if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14")
				|| Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")
				|| Bukkit.getVersion().contains("1.17"))/* ? */ {
			return true;
		}
		return false;
	}

	public static int getFreeGamesInt() {
		int i = 0;
		for (Game game : Main.getInstance().getGames()) {
			if (game.getStatus() == GameStatus.WAITING)
				i++;
		}
		return i;
	}

	public static List<String> getAllCategories() {
		List<String> list = new ArrayList<>();
		for (String s : XPWars.getConfigurator().config.getConfigurationSection("games-gui.categories").getValues(false)
				.keySet()) {
			list.add(s);
		}
		return list;
	}

	public static List<Game> getGamesInCategory(String category) {
		List<Game> list = new ArrayList<>();
		for (String s : XPWars.getConfigurator().config.getStringList("games-gui.categories." + category + ".arenas")) {
			list.add(Main.getGame(s));
		}
		return list;
	}

	public static org.screamingsandals.bedwars.api.game.Game getGameWithHighestPlayersInCategory(String category) {
		TreeMap<Integer, org.screamingsandals.bedwars.api.game.Game> gameList = new TreeMap<>();
		for (org.screamingsandals.bedwars.api.game.Game game : getGamesInCategory(category)) {
			if (game.getStatus() != GameStatus.WAITING) {
				continue;
			}
			if (game.getConnectedPlayers().size() >= game.getMaxPlayers()) {
				continue;
			}
			gameList.put(game.countConnectedPlayers(), game);
		}
		Map.Entry<Integer, org.screamingsandals.bedwars.api.game.Game> lastEntry = gameList.lastEntry();
		return lastEntry.getValue();
	}
}
