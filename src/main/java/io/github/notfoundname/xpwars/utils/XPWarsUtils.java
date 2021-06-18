package io.github.notfoundname.xpwars.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.lib.nms.utils.ClassStorage;
import org.screamingsandals.bedwars.utils.MiscUtils;
import javax.annotation.Nonnull;

import io.github.notfoundname.xpwars.XPWars;

public class XPWarsUtils {

    public static List<String> getShopFileNames() {
        List<String> list = new ArrayList<>();
        List<String> notAllowed = Arrays.asList("config.yml", "sign.yml", "record.yml", "holodb.yml");
        File[] files = Main.getInstance().getDataFolder().listFiles();

        for (File file : files)
            if (file.isFile() && !notAllowed.contains(file.getName()))
                list.add(file.getName());
        return list;
    }

    public static void xpwarsLog(CommandSender sender, String message) {
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && sender instanceof Player)
                message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        } catch (Throwable ignored) { }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&eXPWars&r] " + message));
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
        for (Game game : Main.getInstance().getGames())
            if (game.getStatus() == GameStatus.WAITING)
                i++;
        return i;
    }

    @Nonnull
    public static List<String> getAllCategories() {
        return new ArrayList<>(XPWars.getConfigurator().config.getConfigurationSection("games-gui.categories").getValues(false).keySet());
    }

    public static List<Game> getGamesInCategory(String category) {
        List<Game> list = new ArrayList<>();
        for (String s : XPWars.getConfigurator().config.getConfigurationSection("games-gui.categories." + category)
                .getStringList("arenas"))
            if (Main.isGameExists(s))
                list.add(Main.getGame(s));
        return list.isEmpty() ? null : list;
    }

    public static org.screamingsandals.bedwars.api.game.Game getGameWithHighestPlayersInCategory(String category) {
        TreeMap<Integer, org.screamingsandals.bedwars.api.game.Game> gameList = new TreeMap<>();

        for (org.screamingsandals.bedwars.api.game.Game game : getGamesInCategory(category)) {
            if (game.getStatus() != GameStatus.WAITING
                    || game.countConnectedPlayers() >= game.getMaxPlayers()
                    || game.countConnectedPlayers() == 0)
                continue;
            gameList.put(game.countConnectedPlayers(), game);
        }

        return gameList.lastEntry() == null ?
                getFirstWaitingGameInCategory(category)
                : gameList.lastEntry().getValue();
    }

    public static org.screamingsandals.bedwars.api.game.Game getFirstWaitingGameInCategory(String category) {
        final TreeMap<Integer, Game> availableGames = new TreeMap<>();
        getGamesInCategory(category).forEach(game -> {
            if (game.getStatus() != GameStatus.WAITING)
                return;
            availableGames.put(game.getConnectedPlayers().size(), game);
        });
        return availableGames.isEmpty() ? null : availableGames.lastEntry().getValue();
    }

    @SuppressWarnings("deprecation")
    public static void sendActionBar(Player player, String message) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(player, message);

        if (ClassStorage.IS_PAPER_SERVER && isNewVersion()) player.sendActionBar('&', message);
        else if (ClassStorage.IS_SPIGOT_SERVER)
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
        else MiscUtils.sendActionBarMessage(player, message);
    }

}
