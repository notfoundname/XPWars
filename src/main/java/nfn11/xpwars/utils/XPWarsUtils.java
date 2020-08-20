package nfn11.xpwars.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import nfn11.xpwars.XPWars;

public class XPWarsUtils {

    public static List<String> getShopFileNames() {
        List<String> list = new ArrayList<>();
        List<String> notAllowed = Arrays.asList("config.yml", "sign.yml", "record.yml");
        File[] files = Main.getInstance().getDataFolder().listFiles();

        for (File file : files) {
            if (file.isFile() && !notAllowed.contains(file.getName())) {
                list.add(file.getName());
            }
        }
        return list;
    }

    public static void xpwarsLog(CommandSender sender, String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        msg = "[XPWars] " + msg;
        sender.sendMessage(msg);
    }

    public static List<String> getOnlinePlayers() {
        List<String> list = new ArrayList<>();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> list.add(player.getName()));
        return list;
    }

    public static boolean isNewVersion() {
        return Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14")
                || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")
                || Bukkit.getVersion().contains("1.17");
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
        list.addAll(XPWars.getConfigurator().config.getConfigurationSection("games-gui.categories").getValues(false).keySet());
        return list;
    }

    public static List<Game> getGamesInCategory(String category) {
        List<Game> list = new ArrayList<>();
        for (String s : XPWars.getConfigurator().config.getConfigurationSection("games-gui.categories." + category)
                .getStringList("arenas")) {
            if (Main.isGameExists(s)) {
                list.add(Main.getGame(s));
            }
        }
        if (list.isEmpty())
            return null;
        return list;
    }

    public static org.screamingsandals.bedwars.api.game.Game getGameWithHighestPlayersInCategory(String category) {
        TreeMap<Integer, org.screamingsandals.bedwars.api.game.Game> gameList = new TreeMap<>();
        for (org.screamingsandals.bedwars.api.game.Game game : getGamesInCategory(category)) {
            if (game.getStatus() != GameStatus.WAITING) {
                continue;
            }
            if (game.countConnectedPlayers() >= game.getMaxPlayers()) {
                continue;
            }
            if (game.countConnectedPlayers() == 0) {
                continue;
            }
            gameList.put(game.countConnectedPlayers(), game);
        }
        Map.Entry<Integer, org.screamingsandals.bedwars.api.game.Game> lastEntry = gameList.lastEntry();
        if (lastEntry == null) {
            return getFirstWaitingGameInCategory(category);
        }
        return lastEntry.getValue();
    }

    public static org.screamingsandals.bedwars.api.game.Game getFirstWaitingGameInCategory(String category) {
        final TreeMap<Integer, Game> availableGames = new TreeMap<>();
        getGamesInCategory(category).forEach(game -> {
            if (game.getStatus() != GameStatus.WAITING) {
                return;
            }
            availableGames.put(game.getConnectedPlayers().size(), game);
        });

        if (availableGames.isEmpty()) {
            return null;
        }

        return availableGames.lastEntry().getValue();
    }

    @SuppressWarnings("deprecation")
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
