package io.github.notfoundname.xpwars.placeholderapi;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;

import io.github.notfoundname.xpwars.XPWars;

import java.math.BigInteger;

public class PlaceholderAPIHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private XPWars plugin;

    public PlaceholderAPIHook(XPWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "notfoundname";
    }

    @Override
    public String getIdentifier() {
        return "xpwars";
    }

    @Override
    public String getVersion() {
        return Float.toString(XPWars.getVersion());
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String parsed) {

        if (player == null)
            return "";

        Game game = Main.getGame(parsed.split("_")[1]);
        if (game == null) {
            if (Main.isPlayerInGame(player)) {
                GamePlayer gPlayer = Main.getPlayerGameProfile(player);
                game = gPlayer.getGame();
                CurrentTeam team = game.getPlayerTeam(gPlayer);
                GameStatus status = game.getStatus();

                switch (parsed.toLowerCase()) {
                    case "color":
                        return gPlayer.isSpectator ?
                                ChatColor.GRAY + "" : team != null ?
                                team.teamInfo.color.chatColor + "" : ChatColor.RESET + "";
                    case "state":
                        if (status == GameStatus.WAITING && game.getMinPlayers() <= game.countConnectedPlayers())
                            return ChatColor.translateAlternateColorCodes('&',
                                    XPWars.getConfigurator().config.getString("placeholder-api.state.STARTING", "null")
                                            .replace("%left%", game.getFormattedTimeLeft()));
                        return ChatColor.translateAlternateColorCodes('&',
                                XPWars.getConfigurator().config.getString("placeholder-api.state." + status.name(), "null"));
                    case "bedstate":
                        return ChatColor.translateAlternateColorCodes('&',
                                XPWars.getConfiguration().getString("placeholder-api.bedstate." +
                                        (team.isTargetBlockExists() ? "bedExists" : "bedLost"), "null"));
                    default:
                        return "";
                }
            }
        } else {
            GameStatus status = game.getStatus();
            int gameTime = game.getGameTime();
            int countdown = game.getPauseCountdown();
            switch (parsed.split("_")[0].toLowerCase()) {
                case "ingame":
                    return status == GameStatus.RUNNING || status == GameStatus.GAME_END_CELEBRATING
                            ? Integer.toString(game.countConnectedPlayers()) : "0";
                case "inlobby":
                    return game.getStatus() == GameStatus.WAITING ? Integer.toString(game.countConnectedPlayers()) : "";
                case "lobbyworld":
                    return game.getLobbyWorld().getName();
                case "stateformatted":
                    if (status == GameStatus.WAITING && game.getMinPlayers() <= game.countConnectedPlayers())
                        return ChatColor.translateAlternateColorCodes('&',
                                XPWars.getConfigurator().config.getString("placeholder-api.state.STARTING", "null")
                                        .replace("%left%", game.getFormattedTimeLeft()));
                    return ChatColor.translateAlternateColorCodes('&',
                            XPWars.getConfigurator().config.getString("placeholder-api.state." + status.name(), "null"));
                case "timeleft":
                    return game.getFormattedTimeLeft();
                case "timepassed":
                    return game.getFormattedTimeLeft(gameTime - countdown);
                default:
                    break;
            }
        }
        return null;
    }
    
}
